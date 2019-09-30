package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
// @Conponent @Service @Controller @Repository
@Service
public class SeckillServiceImpl implements SeckillService {

    // sl4f
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // md5盐值字符串，用户混淆MD5
    private final String slat = "kajfajfkajeiraf*jkfja^&fakjf^#$@jkfja%jfaie%";

    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;

    public List<Seckill> getSekillList() {
        return seckillDao.queryAll(0, 4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Transactional
    /**
     * 使用注解控制事务方法的优点：
     * 1：开发 团队达成一致约定，明确事务标注事务方法的编程风格。
     * 2.保证事务方法的执行时间尽可能的短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部
     * 3.不是所有的方法都需要事务，如果只有一条修改操作，只读操作不需要事务控制        当有两条或者两条以上语句修改操作时，才使用事务
     */
    public Exposer exportSeckillURL(long seckillId) {
        Seckill seckill = getById(seckillId);
        if (seckill == null) {
            return new Exposer(false, seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        // 当前时间
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime()
                || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId){
        String base = seckillId +"/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        System.out.println(md5);
        return md5;
    }

    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("sekill data rewrite");
        }
        // 执行秒杀逻辑：减库存 + 记录购买行为
        Date nowTime = new Date();
        // 减库存
        int updateCount = seckillDao.reduceNumber(seckillId, nowTime);

        try {
            if(updateCount <= 0){
                // 没有更新记录，秒杀结束
                throw new SeckillCloseException("Seckill is closed");
            }else{
                // 秒杀成功，记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if(insertCount <= 0){
                    // 重复秒杀
                    throw new RepeatKillException("seckill Repeated");
                }else{
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId,SeckillStatEnum.SUCCESS,successKilled);
                }
            }
        }catch (SeckillCloseException e){
            throw e;
        }catch (RepeatKillException e){
            throw e;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            // 所有编译期异常 转化为运行期异常
            throw  new SeckillException("seckill inner error:"+e.getMessage());
        }
    }
}

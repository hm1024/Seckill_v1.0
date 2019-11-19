package org.seckill.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceImplTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSekillList() {
        List<Seckill> sekillList = seckillService.getSekillList();
        logger.info("list={}",sekillList);
    }

    @Test
    public void getById() {
        long id = 1000L;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}",seckill);

    }

    // 继承测试代码完整逻辑，注意可重复执行
    @Test
    public void seckillLogic() {
        long sekillId = 1001L;
        Exposer exposer = seckillService.exportSeckillURL(sekillId);

        if(exposer.isExposed()){
            long userPhone = 13252012562L;
            String md5 = "e3dab2c3d4d40182b3e5394283b7294c";
            try{
                SeckillExecution execution = seckillService.executeSeckill(sekillId, userPhone, md5);
                logger.info("result={}",execution);
            }catch (RepeatKillException e){
                logger.error(e.getMessage());
            }catch (SeckillCloseException e){
                logger.error(e.getMessage());
            }
        }else{
            // 秒杀未开启
            logger.warn("Expose={}",exposer);
        }

    }

    /**
     * @Author minghai
     * @Description 测试通过存储过程完成秒杀
     * @Date 2019/11/19 18:41
     * @Param []
     * @return void
     */
    @Test
    public void executeSeckillProcedure(){
        long seckillId  = 1001L;
        long phone = 1392349900L;
        Exposer exposer = seckillService.exportSeckillURL(seckillId);
        if(exposer.isExposed()){
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            logger.info(execution.getStateInfo());
        }
    }

}
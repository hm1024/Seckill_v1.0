package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 配置spring和junit整合，Junit启动时加载springIOC容器
 * spring-test，Junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉Junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"} )
public class SeckillDaoTest {

    @Resource
    private SeckillDao seckillDao;

    @Test
    public void queryById() {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void queryAll() {
        /**
         * Caused by: org.apache.ibatis.binding.BindingException:
         * Parameter 'offset' not found. Available parameters are [0, 1, param1, param2]
         */
        //  List<Seckill> queryAll(int offet, int limit);
        // java没有保存形参的记录：queryAll(int offet,int limit)->queryAll(arg0,arg1)
        List<Seckill> seckills = seckillDao.queryAll(0, 100);
        for (Seckill seckill : seckills) {
            System.out.println(seckill);
        }

    }

    @Test
    public void reduceNumber() {
        Date killTime = new Date();
        int count = seckillDao.reduceNumber(100, killTime);
        System.out.println(count);
    }
}
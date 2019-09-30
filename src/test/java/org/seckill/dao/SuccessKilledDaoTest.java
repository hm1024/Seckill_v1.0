package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SuccessKilledDaoTest {
    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    /**
     * 第一次执行：insertCount=1
     * 第二次执行：insertCount=0
     */
    public void insertSuccessKilled() {
        long id = 1001L;
        long phone = 13245649632L;
        int insertCount = successKilledDao.insertSuccessKilled(id, phone);
        System.out.println("insertCount="+insertCount);

    }

    @Test
    public void queryByIdWithSeckill() {
        long id = 1001L;
        long phone = 13245649632L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id, phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }
}
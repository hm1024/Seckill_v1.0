package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SeckillDao {
    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return 如果影响行数>1,表示更新的记录行数
     */
    int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);

    /**
     * 根据id查询Seckill
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param offet 偏移量
     * @param limit 从偏移量开始，取几条
     * @return
     */
    List<Seckill> queryAll(@Param("offet") int offet, @Param("limit") int limit);

    /**
     * @Author minghai
     * @Description 使用存储过程执行秒杀
     * @Date 2019/11/19 17:32
     * @Param [paramMap]
     * @return void
     */
    void killByProcedure(Map<String,Object> paramMap);
}

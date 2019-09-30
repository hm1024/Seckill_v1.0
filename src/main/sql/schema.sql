-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE seckill;
-- 使用数据库
USE seckill;
-- 创建秒杀库存表
CREATE TABLE seckill(
`seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
`name` VARCHAR(120) NOT NULL COMMENT '商品的名称',
`number` INT NOT NULL COMMENT '库存数量',
`start_time` timestamp NOT NULL DEFAULT 0 COMMENT '秒杀开启时间',
`end_time` timestamp NOT NULL COMMENT '秒杀结束时间',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY PK_SECKILL_ID(seckill_id),
KEY idx_start_time(start_time),
KEY idx_end_time(end_time),
KEY idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT '秒杀库存表';


-- 初始化数据
INSERT INTO seckill(name,number,start_time,end_time)
VALUES
  ('1000元秒杀iphone11',100,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
  ('900元秒杀ipad pro',200,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
  ('2000元秒杀mate30',300,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
  ('2500元秒杀macbook',400,'2015-11-01 00:00:00','2015-11-02 00:00:00');

-- 秒杀成功明细表
-- 用户登录认证相关的信息
CREATE TABLE success_killed(
seckill_id bigint NOT NULL COMMENT '秒杀商品id',
user_phone bigint NOT NULL COMMENT '用户手机号',
state TINYINT NOT NULL DEFAULT -1 COMMENT '状态标识：-1：无效 0：成功 1：已付款 2:已发货',
create_time TIMESTAMP NOT NULL COMMENT '创建时间',
PRIMARY KEY (seckill_id,user_phone),/*联合主键*/
KEY idx_create_time(create_time)
)ENGINE=INNODB DEFAULT CHARSET utf8 COMMENT '秒杀成功明细表';

-- 连接数据库
mysql -uroot -proot

-- 为什么手写DDL
-- 记录每次上线的DDL修改
-- 上线V1.1


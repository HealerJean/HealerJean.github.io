-- 踩坑
-- 1、h2 不允许 bigint(20) int(11) 不允许为这些设置基本参数
-- 2、不允许指定 engine = innodb mysql引擎
-- 3、不支持 bigint unsigned
create table if not exists `user_demo`
(
    `id`          bigint      not null auto_increment comment '主键',
    `name`        varchar(32) not null default '' comment '姓名',
    `age`         int         not null default 0 comment '年龄',
    `phone`       varchar(32) not null default '' comment '电话',
    `email`       varchar(64) not null default '' comment '邮箱',
    `start_time`  datetime             default null comment '开始时间',
    `end_time`    datetime             default null comment '结束时间',
    `valid_flag`  int         not null default 1 comment '1有效 0 废弃',
    `create_time` datetime    not null default current_timestamp comment '创建时间',
    `update_time` datetime    not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`id`)
);



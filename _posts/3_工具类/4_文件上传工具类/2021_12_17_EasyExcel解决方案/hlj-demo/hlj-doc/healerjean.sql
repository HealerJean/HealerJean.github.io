create table if not exists `user_demo`
(
    `id`          bigint unsigned not null auto_increment comment '主键',
    `name`        varchar(32)     not null default '' comment '姓名',
    `age`         int             not null default 0 comment '年龄',
    `phone`       varchar(32)     not null default '' comment '电话',
    `email`       varchar(64)     not null default '' comment '邮箱',
    `start_time`  datetime                 default null comment '开始时间',
    `end_time`    datetime                 default null comment '结束时间',
    `valid_flag`  int             not null default 1 comment '1有效 0 废弃',
    `create_time` datetime        not null default current_timestamp comment '创建时间',
    `update_time` datetime        not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`id`)
) engine = innodb
  default charset = utf8;

drop table if exists user_demo;
create table `user_demo`
(
    id            bigint(20) unsigned not null auto_increment comment '主键',
    `name`        varchar(32)         not null default '' comment '姓名',
    `age`         int(11)             not null default 0 comment '年龄',
    `tel_phone`   varchar(20)         not null default '' comment '电话',
    `email`       varchar(50)         not null default '' comment '邮箱',
    `create_time` datetime            not null default current_timestamp comment '创建时间',
    `update_time` datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`id`)
) engine = innodb
  default charset = utf8;

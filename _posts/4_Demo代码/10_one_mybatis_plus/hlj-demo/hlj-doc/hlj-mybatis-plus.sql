drop table if exists user;
create table `user`
(
    `id`          bigint(20)  not null comment '主键id',
    `name`        varchar(30) default null comment '姓名',
    `age`         int(11)     default null comment '年龄',
    tel_phone     varchar(20) default null comment '电话',
    `email`       varchar(50) default null comment '邮箱',
    `create_date` date        default null comment '日期',
    `create_time` datetime    default null comment '年月日',
    primary key (`id`)
) engine = innodb
  default charset = utf8;

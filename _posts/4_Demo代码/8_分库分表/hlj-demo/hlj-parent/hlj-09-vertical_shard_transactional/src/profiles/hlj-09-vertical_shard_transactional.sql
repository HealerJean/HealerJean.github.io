drop database if exists ds_0;
create database ds_0 character set 'utf8' collate 'utf8_general_ci';
use ds_0;

drop table if exists user;
create table `user`
(
  `id`        bigint(20) unsigned not null,
  city        varchar(20)         not null default '',
  name        varchar(20)         not null default '',
  status      int(10)             not null default '0' comment '状态',
  create_time datetime            not null default current_timestamp comment '创建时间',
  update_time datetime            not null default current_timestamp on update current_timestamp comment '修改时间',
  primary key (`id`)
) engine = innodb
  default charset = utf8;


create table `demo_entity`
(
  `id`          bigint(20) unsigned not null comment '主键',
  `name`        varchar(64)         not null,
  `phone`       varchar(20)                  default '' comment '手机号',
  `email`       varchar(64)                  default '' comment '邮箱',
  `age`         int(10)                      default null,
  `status`      varchar(8)          not null comment '状态',
  `create_user` bigint(16) unsigned          default null comment '创建人',
  `create_name` varchar(64)                  default '' comment '创建人名称',
  `create_time` datetime            not null default current_timestamp comment '创建时间',
  `update_user` bigint(16) unsigned          default null comment '更新人',
  `update_name` varchar(64)                  default '' comment '更新人名称',
  `update_time` datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
  primary key (`id`)
) engine = innodb
  default charset = utf8;


drop database if exists ds_1;
create database `ds_1` character set 'utf8' collate 'utf8_general_ci';
use ds_1;

drop table if exists company;
create table `company`
(
  `id`                 bigint(20) unsigned not null comment '主键',
  name                 varchar(20)         not null default '' comment '企业名称',
  company_name_english varchar(128)        not null default '' comment '企业英文名称',
  status               int(10)             not null default '0' comment '状态',
  create_time          datetime            not null default current_timestamp comment '创建时间',
  update_time          datetime            not null default current_timestamp on update current_timestamp comment '修改时间',

  primary key (`id`)
) engine = innodb
  default charset = utf8;

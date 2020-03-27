drop database if exists ds_0;
create database ds_0 character set 'utf8' collate 'utf8_general_ci';
use ds_0;

drop table if exists user_0;
create table `user_0`
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

drop table if exists user_1;
create table `user_1`
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

drop table if exists user_2;
create table `user_2`
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

drop table if exists user_3;
create table `user_3`
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






drop table if exists company_0;
create table `company_0`
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

drop table if exists company_1;
create table `company_1`
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








CREATE TABLE `demo_entity`
(
  `id`          bigint(20) unsigned NOT NULL COMMENT '主键',
  `name`        varchar(64)         NOT NULL,
  `phone`       varchar(20)                  DEFAULT '' COMMENT '手机号',
  `email`       varchar(64)                  DEFAULT '' COMMENT '邮箱',
  `age`         int(10)                      DEFAULT NULL,
  `status`      varchar(8)          NOT NULL COMMENT '状态',
  `create_user` bigint(16) unsigned          DEFAULT NULL COMMENT '创建人',
  `create_name` varchar(64)                  DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(16) unsigned          DEFAULT NULL COMMENT '更新人',
  `update_name` varchar(64)                  DEFAULT '' COMMENT '更新人名称',
  `update_time` datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

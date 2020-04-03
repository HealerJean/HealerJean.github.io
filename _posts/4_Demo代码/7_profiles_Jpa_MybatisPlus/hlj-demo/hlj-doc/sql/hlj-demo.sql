 create table `demo_entity` (
 `id` bigint(16) unsigned not null auto_increment,
 `name` varchar(64) not null,
 `phone` varchar(20) default '' comment '手机号',
 `email` varchar(64) default '' comment '邮箱',
 `age` int(100) default null,
 `status` varchar(8) not null comment '10可用，99删除',
 `create_user` bigint(16) unsigned default null comment '创建人',
 `create_name` varchar(64) default '' comment '创建人名称',
 `create_time` timestamp not null default current_timestamp comment '创建时间',
 `update_user` bigint(16) unsigned default null comment '更新人',
 `update_name` varchar(64) default '' comment '更新人名称',
 `update_time` timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
 primary key (`id`)
 ) engine=innodb default charset=utf8;

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


drop table file_task ;
create table `file_task`
(
    `id`             bigint(20)    not null auto_increment comment '主键标识列',
    `user_id`        varchar(64)  not null comment '用户Id',
    `task_id`        varchar(255)  not null comment '任务唯一id',
    `task_type`      varchar(32)   not null comment 'export 导出，import导入',
    `business_type`  varchar(32)   not null comment '业务类型',
    `business_data`  varchar(1024) not null comment '业务请求数据',
    `task_status`    varchar(32)   not null comment 'processing 处理中，completed 完成，fail 失败',
    `result_url`     varchar(1024) not null default '' comment '返回的url地址',
    `result_message` varchar(1024) not null default '' comment '处理结果',
    `url`            varchar(255)  not null default '' comment '上传文件地址',
    `ext`            varchar(1024) not null default '' comment '',
    `created_time`   datetime     not null default current_timestamp comment '记录创建时间',
    `modified_time`  datetime     not null default current_timestamp on update current_timestamp comment '记录最后更新时间',
    primary key (`id`),
    unique key uk_task_id(`task_id`),
    key `idx_user_modified_time` (`user_id`, `modified_time`),
    key `idx_task` (`task_id`),
    key `idx_created_time` (`created_time`),
    key `idx_modified_time` (`modified_time`)
) engine = innodb comment ='文件任务'
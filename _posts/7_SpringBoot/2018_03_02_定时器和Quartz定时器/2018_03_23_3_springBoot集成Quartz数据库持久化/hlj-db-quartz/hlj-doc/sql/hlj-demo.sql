drop table if exists schedule_job ;
create table `schedule_job`
(
    `id`        bigint(20) unsigned not null ,
    `cron`      varchar(255) collate utf8_bin default '' comment '时间表达式',
    `job_name`  varchar(255) collate utf8_bin default '' comment '任务名称',
    `job_desc`  varchar(255) collate utf8_bin default '' comment '任务详情',
    `job_class` varchar(255) collate utf8_bin default '' comment '任务类全名，包含包',
    `status`    varchar(255) collate utf8_bin default '' comment '99 废弃 10 有效',
    primary key (`id`)
);

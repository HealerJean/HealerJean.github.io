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
);

select * from user_demo;

# 客户端中可以执行测试的存储过程代码
drop procedure if exists test_batch_create;
create procedure test_batch_create(in loop_counts int, in date varchar(50))
begin
    declare i int;
    set i = 0;
    set autocommit = 0; -- 关闭自动提交事务，提高插入效率
    while i < loop_counts
        do
            insert into user_demo (name, age, phone, email, start_time, end_time, valid_flag)
            values (concat('张', floor(rand() * 2 * i)), floor(rand() * i), floor(rand() * 3 * i),
                    concat(floor(rand() * 2 * i), '@gmail.com'), date_add(date, interval i day),
                    date_add(date, interval i * 2 day), 1);
            set i = i + 1;
        end while;
    commit;
end;


CALL test_batch_create(10, '2023-07-01');
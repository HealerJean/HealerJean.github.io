
use hlj_activemq_trans_1;
drop table if exists t_event;
create table t_event
(
  id          bigint(20) unsigned not null auto_increment comment '主键',
  type        varchar(30)         not null default '' comment '事件的类型：比如新增用户、新增积分',
  process     varchar(30)         not null default '' comment '事件进行到的环节：比如，新建、已发布、已处理',
  content     varchar(255)        not null default '' comment '事件的内容，用于保存事件发生时需要传递的数据',
  create_time datetime            not null default current_timestamp comment '创建时间',
  update_time datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
  primary key (id)
) comment 'mq分布式事务-事件表';


drop table if exists  t_user;
create table t_user
(
  id        bigint(20) unsigned not null auto_increment comment '主键',
  user_name varchar(64)         not null default '' comment '用户名',
  primary key (id)
) comment 'mq分布式事务-用户表';




use hlj_activemq_trans_2;
drop table if exists t_event;
create table t_event
(
  id          bigint(20) unsigned not null auto_increment comment '主键',
  type        varchar(30)         not null default '' comment '事件的类型：比如新增用户、新增积分',
  process     varchar(30)         not null default '' comment '事件进行到的环节：比如，新建、已发布、已处理',
  content     varchar(255)        not null default '' comment '事件的内容，用于保存事件发生时需要传递的数据',
  create_time datetime            not null default current_timestamp comment '创建时间',
  update_time datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
  primary key (id)
) comment 'mq分布式事务-事件表';


drop table if exists  t_point;
create table t_point
(
  id      bigint(20) unsigned not null auto_increment comment '主键',
  user_id bigint(20) unsigned not null default 0 comment '关联的用户id',
  amount  decimal(19, 2)      not null default 0 comment '积分金额',
  primary key (id)
)comment 'mq分布式事务-积分表';



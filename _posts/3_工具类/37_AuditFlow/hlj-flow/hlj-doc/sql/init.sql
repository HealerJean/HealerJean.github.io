drop table if exists sys_department;
create table sys_department
(
    id          bigint(20)          not null auto_increment comment '主键',
    name        varchar(64)         not null default '' comment '部门名称',
    description varchar(64)         not null default '' comment '部门描述',
    pid         bigint(20)          not null default '0' comment '父部门',
    status      varchar(8)          not null default '' comment '状态: 10：有效，99：无效',
    create_user bigint(20) unsigned not null default '0' comment '创建人id',
    create_name varchar(64)         not null default '' comment '创建人名称',
    create_time datetime            not null default current_timestamp comment '创建时间',
    update_user bigint(20) unsigned not null default '0' comment '更新人',
    update_name varchar(64)         not null default '' comment '更新人名称',
    update_time datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree
) engine = innodb comment ='系统模块-部门表';


drop table if exists sys_dictionary_data;
create table sys_dictionary_data
(
    id           bigint(20) unsigned not null auto_increment comment '主键',
    data_key     varchar(32)         not null comment '数据类型type_key  表dictionary_type',
    data_value   varchar(64)         not null comment '字典数据 描述',
    ref_type_key varchar(32)         not null comment '数据类型type_key  表dictionary_type',
    sort         int(11) unsigned    not null default '0' comment '排序',
    status       varchar(8)          not null comment '状态',
    create_user  bigint(20) unsigned not null default '0' comment '创建人',
    create_name  varchar(64)         not null default '' comment '创建人名称',
    create_time  datetime            not null default current_timestamp comment '创建时间',
    update_user  bigint(20) unsigned not null default '0' comment '更新人',
    update_name  varchar(64)         not null default '' comment '更新人名称',
    update_time  datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree,
    unique key uk_typekey_datakey (ref_type_key, data_key) using btree
) engine = innodb comment ='系统模块-字典表数据';


drop table if exists sys_dictionary_type;
create table sys_dictionary_type
(
    id          bigint(20) unsigned not null auto_increment comment '主键',
    type_key    varchar(32)         not null comment '字典类型',
    type_desc   varchar(64)         not null comment '字典类型 描述',
    status      varchar(8)          not null comment '状态',
    create_user bigint(20) unsigned not null default '0' comment '创建人',
    create_name varchar(64)         not null default '' comment '创建人名称',
    create_time datetime            not null default current_timestamp comment '创建时间',
    update_user bigint(20) unsigned not null default '0' comment '更新人',
    update_name varchar(64)         not null default '' comment '更新人名称',
    update_time datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree,
    unique key uk_typekey (type_key) using btree
) engine = innodb comment ='系统模块-字典表类型';


drop table if exists sys_district;
create table sys_district
(
    id            bigint(20) unsigned not null auto_increment comment '主键',
    province_code varchar(8)          not null default '' comment '省-编码',
    province_name varchar(64)         not null default '' comment '省-名称',
    city_code     varchar(8)          not null default '' comment '城市-编码',
    city_name     varchar(64)         not null default '' comment '城市-名称',
    district_code varchar(8)          not null default '' comment '区/县-编码',
    district_name varchar(64)         not null default '' comment '区/县-名称',
    status        varchar(8)          not null default '' comment '状态: 10：有效，99：无效',
    update_time   datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree,
    key idx_province (province_code) using btree comment '省份-索引',
    key idx_city (city_code) using btree comment '城市-索引',
    key idx_district (district_code) using btree comment '地区-索引'
) engine = innodb comment ='系统模块-地区信息表';


drop table if exists sys_email_log;
create table sys_email_log
(
    id            bigint(20) unsigned not null auto_increment comment '主键',
    type          varchar(32)         not null comment '邮件类型 数据字典',
    subject       varchar(64)         not null comment '邮件标题',
    content       text                not null comment '邮件内容',
    send_email    varchar(64)         not null comment '发送邮箱',
    receive_mails varchar(255)        not null comment '接收邮箱',
    status        varchar(8)          not null comment 'success 发送成功 ，error 发送失败 ',
    msg           varchar(255)        not null comment '发送状态信息 发送成功、异常信息',
    create_time   datetime            not null default current_timestamp comment '创建时间',
    update_time   datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree
) engine = innodb comment ='系统模块-邮件记录表';


drop table if exists sys_menu;
create table sys_menu
(
    id              bigint(20) unsigned not null auto_increment comment '主键',
    ref_system_code varchar(32)         not null comment '系统code',
    menu_name       varchar(64)         not null comment '菜单名称',
    url             varchar(255)        not null default '' comment '菜单调用地址',
    method          varchar(32)         not null default '' comment '调用方法（get，post，put，delete）',
    pid             bigint(20) unsigned not null default '0' comment '父级id',
    pchain          varchar(255)        not null default '' comment '父链id，“,”分隔',
    description     varchar(255)        not null default '' comment '描述',
    sort            int(11) unsigned    not null default '0' comment '显示排序',
    icon            varchar(255)        not null default '' comment '菜单图标',
    front_key       varchar(32)         not null default '' comment '前端菜单标识（前端菜单唯一标识）',
    is_permission   varchar(2)          not null default '' comment '是否需要权限拦截，10：需要，99：不需要',
    menu_type       varchar(2)          not null default '' comment '菜单类型：0: 后端路径, 1:前端菜单，2:非展示前端菜单',
    status          varchar(8)          not null comment '状态',
    create_user     bigint(20) unsigned not null default '0' comment '创建人',
    create_name     varchar(64)         not null default '' comment '创建人名称',
    create_time     datetime            not null default current_timestamp comment '创建时间',
    update_user     bigint(20) unsigned not null default '0' comment '更新人',
    update_name     varchar(64)         not null default '' comment '更新人名称',
    update_time     datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree
) engine = innodb comment ='系统模块-菜单表';


drop table if exists sys_role;
create table sys_role
(
    id              bigint(20) unsigned not null auto_increment comment '主键',
    role_name       varchar(64)         not null comment '角色名称',
    ref_system_code varchar(32)         not null comment '系统code',
    description     varchar(255)        not null default '' comment '描述',
    status          varchar(8)          not null comment '状态',
    create_user     bigint(20) unsigned not null default '0' comment '创建人',
    create_name     varchar(64)         not null default '' comment '创建人名称',
    create_time     datetime            not null default current_timestamp comment '创建时间',
    update_user     bigint(20) unsigned not null default '0' comment '更新人',
    update_name     varchar(64)         not null default '' comment '更新人名称',
    update_time     datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree
) engine = innodb comment ='系统模块-角色表';



drop table if exists sys_role_menu_ref;
create table sys_role_menu_ref
(
    id          bigint(20) unsigned not null auto_increment comment '主键',
    ref_role_id bigint(20) unsigned not null comment '角色id',
    ref_menu_id bigint(20) unsigned not null comment '菜单id',
    status      varchar(8)          not null comment '状态',
    create_user bigint(20) unsigned not null default '0' comment '创建人',
    create_name varchar(64)         not null default '' comment '创建人名称',
    create_time datetime            not null default current_timestamp comment '创建时间',
    update_user bigint(20) unsigned not null default '0' comment '更新人',
    update_name varchar(64)         not null default '' comment '更新人名称',
    update_time datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree,
    key idx_role_id (ref_role_id) using btree comment '角色id索引'
) engine = innodb comment ='系统模块-角色与菜单关系表';



drop table if exists sys_template;
create table sys_template
(
    id          bigint(20) unsigned not null auto_increment comment '主键',
    name        varchar(32)         not null comment '模板名称',
    content     text                not null comment '模板内容',
    type        varchar(32)         not null comment '业务类型（邮箱，合同，短信等） 字典表',
    description varchar(255)        not null default '' comment '模板描述',
    status      varchar(8)          not null comment '状态',
    create_user bigint(20) unsigned not null default '0' comment '创建人',
    create_name varchar(64)         not null default '' comment '创建人名称',
    create_time datetime            not null default current_timestamp comment '创建时间',
    update_user bigint(20) unsigned not null default '0' comment '更新人',
    update_name varchar(64)         not null default '' comment '更新人名称',
    update_time datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree
) engine = innodb comment ='系统模块-模板';


drop table if exists sys_user_department_ref;
create table sys_user_department_ref
(
    id                bigint(20) unsigned not null auto_increment comment '主键',
    ref_user_id       bigint(20)          not null comment '用户id',
    ref_department_id bigint(20)          not null comment '部门id',
    status            varchar(8)          not null default '' comment '状态: 10：有效，99：无效',
    update_time       datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree
) engine = innodb comment ='系统模块-用户与部门关系表';


drop table if exists sys_user_info;
create table sys_user_info
(
    id          bigint(20)          not null auto_increment,
    user_name   varchar(64)         not null comment '用户名',
    real_name   varchar(64)         not null comment '真实姓名',
    email       varchar(64)         not null comment '邮箱',
    telephone   varchar(20)         not null comment '手机号',
    password    varchar(128)        not null comment '密码',
    user_type   varchar(32)         not null comment '用户类型',
    status      varchar(32)         not null comment '用户状态',
    salt        varchar(128)        not null comment '密码随机盐',
    create_user bigint(20) unsigned not null default '0' comment '创建人',
    create_name varchar(64)         not null default '' comment '创建人名称',
    create_time datetime            not null default current_timestamp comment '创建时间',
    update_user bigint(20) unsigned not null default '0' comment '更新人',
    update_name varchar(64)         not null default '' comment '更新人名称',
    update_time datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree,
    unique key uk_username (user_name) using btree comment '用户名唯一',
    unique key uk_email (email) using btree comment '邮箱唯一'
) engine = innodb comment ='系统模块-平台用户';



drop table if exists sys_user_role_ref;
create table sys_user_role_ref
(
    id          bigint(20) unsigned not null auto_increment comment '主键',
    ref_user_id bigint(20) unsigned not null comment '用户id',
    ref_role_id bigint(20) unsigned not null comment '角色id',
    status      varchar(8)          not null comment '状态',
    create_user bigint(20) unsigned not null default '0' comment '创建人',
    create_name varchar(64)         not null default '' comment '创建人名称',
    create_time datetime            not null default current_timestamp comment '创建时间',
    update_user bigint(20) unsigned not null default '0' comment '更新人',
    update_name varchar(64)         not null default '' comment '更新人名称',
    update_time datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree,
    key idx_user_id (ref_user_id) using btree comment '用户id索引'
) engine = innodb comment ='系统模块-用户与角色关系表';




drop table if exists flow_audit_task;
create table flow_audit_task
(
    id          bigint(20) unsigned not null auto_increment comment '主键',
    task_type   varchar(64)         not null default '' comment '任务类型',
    task_name   varchar(64)         not null default '' comment '任务名字',
    task_data   varchar(3000)       not null default '' comment '任务数据Json格式',
    status      varchar(8)          not null default '' comment '审批状态',
    create_time datetime            not null default current_timestamp comment '创建时间',
    create_user bigint(20) unsigned not null default '0' comment '创建人,发起人',
    create_name varchar(64)         not null default '' comment '创建人名称',
    update_user bigint(20) unsigned not null default 0 comment '更新人',
    update_name varchar(64)         not null default '' comment '更新人名称',
    update_time datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree
) engine = innodb comment ='工作流-审核任务表';


drop table if exists flow_audit_default_config;
create table flow_audit_default_config
(
    id          bigint(20) unsigned not null auto_increment comment '主键',
    task_type   varchar(64)         not null default '' comment '任务类型',
    task_name   varchar(64)         not null default '' comment '任务名字',
    modify      tinyint(1)          not null comment '是否可以更改默认审批人',
    status      varchar(8)          not null default '' comment '状态10有效 99废弃',
    create_user bigint(20) unsigned not null default 0 comment '创建人',
    create_name varchar(64)         not null default '' comment '创建人名称',
    create_time datetime            not null default current_timestamp comment '创建时间',
    update_user bigint(20) unsigned not null default 0 comment '更新人',
    update_name varchar(64)         not null default '' comment '更新人名称',
    update_time datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree
) engine = innodb comment ='工作流-默认审批是否可以修改';


drop table if exists flow_audit_default_user;
create table flow_audit_default_user
(
    id                bigint(20) unsigned not null auto_increment comment '主键',
    task_type         varchar(64)         not null default '' comment '任务类型',
    task_name         varchar(64)         not null default '' comment '任务名字',
    audit_user_type   varchar(64)         not null default '' comment '审批类型属性：抄送人、审批人类型',
    audit_object_type varchar(32)         not null default '' comment '审批对象类型：角色或ID',
    audit_object_id   bigint(20) unsigned not null default 0 comment '审批对象对应的id',
    step              int(10) unsigned    not null default '0' comment '处理顺序，抄送人0',
    status            varchar(8)          not null default '' comment '状态',
    create_user       bigint(20) unsigned not null default 0 comment '创建人',
    create_name       varchar(64)         not null default '' comment '创建人名称',
    create_time       datetime            not null default current_timestamp comment '创建时间',
    update_user       bigint(20) unsigned not null default 0 comment '更新人',
    update_name       varchar(64)         not null default '' comment '更新人名称',
    update_time       datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree
) engine = innodb comment ='工作流-默认审批人';

drop table if exists flow_audit_user_detail;
create table flow_audit_user_detail
(
    id                bigint(20) unsigned not null auto_increment comment '主键',
    ref_audit_task_id bigint(20) unsigned not null comment '审批任务Id',
    task_type         varchar(64)         not null default '' comment '任务类型',
    task_name         varchar(64)         not null default '' comment '任务名字',
    step              int(10) unsigned    not null default '0' comment '审批到了第几步，抄送人0',
    audit_user_type   varchar(64)         not null default '' comment '抄送人、审批人类型',
    audit_object_type varchar(64)         not null default '' comment '审批对象类型：角色或ID',
    audit_object_id   bigint(20) unsigned not null default 0 comment '审批对象对应的id',
    status            varchar(8)          not null default '' comment '审批状态',
    create_user       bigint(20) unsigned not null default 0 comment '创建人',
    create_name       varchar(64)         not null default '' comment '创建人名称',
    create_time       datetime            not null default current_timestamp comment '创建时间',
    update_user       bigint(20) unsigned not null default 0 comment '更新人',
    update_name       varchar(64)         not null default '' comment '更新人名称',
    update_time       datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree
) engine = innodb comment ='工作流-审核详情';

drop table if exists flow_audit_record;
create table flow_audit_record
(
    id                bigint(20) unsigned not null auto_increment comment '主键',
    ref_audit_task_id bigint(20) unsigned not null comment '审批任务Id',
    task_type         varchar(64)         not null default '' comment '任务类型',
    task_name         varchar(64)         not null default '' comment '任务名字',
    step              int(10) unsigned    not null default '0' comment '审批到了第几步，抄送人0',
    audit_message     varchar(128)        not null default '' comment '审批意见',
    ref_file_ids      varchar(128)        not null default '' comment '审批附件Josn集合',
    audit_user_type   varchar(64)         not null default '' comment '抄送人、审批人类型',
    opt_user__type    varchar(64)         not null default '' comment '审批对象类型：角色或ID',
    audit_object_id   bigint(20) unsigned not null default 0 comment '审批对象对应的id',
    opt_user_id       bigint(20) unsigned not null default 0 comment '审批人Id',
    opt_user_name     varchar(32)         not null default '' comment '审批人名字',
    status            varchar(8)          not null default '' comment '审批状态',
    create_time       datetime            not null default current_timestamp comment '创建时间',
    primary key (id) using btree
) engine = innodb comment ='工作流-审核记录';



drop table if exists  flow_work_node ;
create table flow_work_node
(
    id          bigint(20) unsigned not null auto_increment comment '主键',
    node_code   varchar(32)         not null default '' comment '节点编号',
    node_name   varchar(64)         not null default '' comment '节点名称',
    node_type   varchar(32)         not null default '' comment '流程节点或者审核节点',
    status      varchar(8)          not null default '' comment '状态',
    create_user bigint(20) unsigned not null default '0' comment '创建人',
    create_name varchar(64)         not null default '' comment '创建人名称',
    create_time datetime            not null default current_timestamp comment '创建时间',
    update_user bigint(20) unsigned not null default '0' comment '更新人',
    update_name varchar(64)         not null default '' comment '更新人名称',
    update_time datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree
) engine = innodb comment ='工作流-流程节点表';



drop table if exists  flow_work_default_definition ;
create table flow_work_default_definition
(
    id              bigint(20) unsigned not null auto_increment comment '主键',
    flow_code       varchar(64)         not null default '' comment '工作流编码',
    flow_name       varchar(64)         not null default '' comment '工作流名称',
    flow_definition varchar(255)        not null default '' comment '节点顺序流程',
    status          varchar(8)          not null default '' comment '状态',
    create_user     bigint(20) unsigned not null default '0' comment '创建人',
    create_name     varchar(64)         not null default '' comment '创建人名称',
    create_time     datetime            not null default current_timestamp comment '创建时间',
    update_user     bigint(20) unsigned not null default '0' comment '更新人',
    update_name     varchar(64)         not null default '' comment '更新人名称',
    update_time     datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree
) engine = innodb comment ='工作流-默认流程定义表';


drop table if exists  flow_work_definition ;
create table flow_work_definition
(
    id              bigint(20) unsigned not null auto_increment comment '主键',
    instants_no     varchar(64)         not null default '' comment '流程实例流水号',
    flow_code       varchar(64)         not null default '' comment '工作流编码',
    flow_name       varchar(64)         not null default '' comment '工作流名称',
    flow_definition varchar(255)        not null default '' comment '节点顺序流程',
    status          varchar(8)          not null default '' comment '状态',
    create_user     bigint(20) unsigned not null default '0' comment '创建人',
    create_name     varchar(64)         not null default '' comment '创建人名称',
    create_time     datetime            not null default current_timestamp comment '创建时间',
    update_time     datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree
) engine = innodb comment ='工作流-流程定义表';


drop table if exists  flow_work_record ;
create table flow_work_record
(
    id          bigint(20) unsigned not null auto_increment comment '主键',
    instants_no varchar(64)         not null default '' comment '流程实例流水号',
    flow_code   varchar(64)         not null default '' comment '工作流节点编码',
    flow_name   varchar(64)         not null default '' comment '工作流节点名称',
    step        int(10) unsigned    not null default '0' comment '流程步骤',
    node_code   varchar(32)         not null default '' comment '节点编号',
    node_name   varchar(64)         not null default '' comment '节点名称',
    status      varchar(8)          not null default '' comment '状态',
    create_user bigint(20) unsigned not null default '0' comment '创建人',
    create_name varchar(64)         not null default '' comment '创建人名称',
    create_time datetime            not null default current_timestamp comment '创建时间',
    update_time datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree
) engine = innodb  comment ='工作流-流程节点表';



drop table if exists  flow_work_audit_ref ;
create table flow_work_audit_ref
(
    id          bigint(20) unsigned not null auto_increment comment '主键',
    ref_audit_task_id bigint(20) unsigned not null comment '审批任务Id',
    instants_no varchar(64)         not null default '' comment '流程实例流水号',
    step        int(10) unsigned    not null default '0' comment '流程步骤',
    node_code   varchar(32)         not null default '' comment '节点编号',
    status      varchar(8)          not null default '' comment '状态',
    create_user bigint(20) unsigned not null default '0' comment '创建人',
    create_name varchar(64)         not null default '' comment '创建人名称',
    create_time datetime            not null default current_timestamp comment '创建时间',
    update_time datetime            not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id) using btree
) engine = innodb  comment ='工作流-流程节点和审核的关系表';






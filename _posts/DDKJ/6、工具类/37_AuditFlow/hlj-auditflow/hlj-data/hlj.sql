
-- ----------------------------
-- Table structure for scf_flow_audit_record
-- ----------------------------
drop table if exists `scf_flow_audit_record`;
create table `scf_flow_audit_record`  (
  `id` bigint(20) unsigned not null AUTO_INCREMENT comment '主键',
  `ref_file_ids` varchar(128)  null default '' comment '审批附件',
  `instants_no` varchar(64)  null default '' comment '流程实例流水号',
  `sept` int(10) unsigned null default 0 comment '流程步骤',
  `flow_code` varchar(64)  null default '' comment '节点编码',
  `flow_name` varchar(64)  null default '' comment '节点名称',
  `node_code` varchar(32)  null default '' comment '节点编号',
  `node_name` varchar(64)  null default '' comment '节点名称',
  `audit_sept` int(10) unsigned null default 0 comment '审核步骤',
  `status` varchar(8)  null default '' comment '状态',
  `opt_user` bigint(20) unsigned null default 0 comment '执行人',
  `opt_user_name` varchar(64)  null default '' comment '执行人真实名称',
  `opt_time` datetime null default current_timestamp comment '执行时间',
  `audit_message` varchar(128)  null default '' comment '审批意见',
  `create_time` datetime not null default current_timestamp comment '创建时间',
  `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
  primary key (`id`) using btree
) engine = innodb   comment = '审核记录表';

-- ----------------------------
-- Table structure for scf_flow_audit_record_temp
-- ----------------------------
drop table if exists `scf_flow_audit_record_temp`;
create table `scf_flow_audit_record_temp`  (
  `id` bigint(20) unsigned not null AUTO_INCREMENT comment '主键',
  `instants_no` varchar(64)  null default '' comment '流程实例流水号',
  `sept` int(10) unsigned null default 0 comment '流程步骤',
  `flow_code` varchar(64)  null default '' comment '节点编码',
  `flow_name` varchar(64)  null default '' comment '节点名称',
  `node_code` varchar(32)  null default '' comment '节点编号',
  `node_service_type` varchar(64)  null default '' comment '节点业务类型',
  `node_name` varchar(64)  null default '' comment '节点名称',
  `audit_sept` int(10) unsigned null default 0 comment '审核步骤',
  `audit_type` varchar(64)  null default '' comment '审核类型：角色或ID',
  `audit_object` bigint(20) unsigned null default 0 comment '审核对象',
  `audit_data` varchar(3000)  null default '' comment '审核数据序列化',
  `status` varchar(8)  null default '' comment '状态',
  `create_user` bigint(20) unsigned null default 0 comment '创建人',
  `create_name` varchar(64)  null default '' comment '创建人名称',
  `create_time` datetime not null default current_timestamp comment '创建时间',
  `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
  primary key (`id`) using btree
) engine = innodb        comment = '审核记录临时表';

-- ----------------------------
-- Table structure for scf_flow_definition
-- ----------------------------
drop table if exists `scf_flow_definition`;
create table `scf_flow_definition`  (
  `id` bigint(20) unsigned not null AUTO_INCREMENT comment '主键',
  `flow_code` varchar(64)  null default '' comment '节点编码',
  `flow_name` varchar(64)  null default '' comment '节点名称',
  `flow_definition` varchar(255)  null default '' comment '节点内部定义（目前主要是审核节点使用）',
  `status` varchar(8)  null default '' comment '状态',
  `create_user` bigint(20) unsigned null default 0 comment '创建人',
  `create_name` varchar(64)  null default '' comment '创建人名称',
  `create_time` datetime not null default current_timestamp comment '创建时间',
  `update_user` bigint(20) unsigned null default 0 comment '更新人',
  `update_name` varchar(64)  null default '' comment '更新人名称',
  `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
  primary key (`id`) using btree
) engine = innodb  comment = '流程定义表';

-- ----------------------------
-- Table structure for scf_flow_node
-- ----------------------------
drop table if exists `scf_flow_node`;
create table `scf_flow_node`  (
  `id` bigint(20) unsigned not null AUTO_INCREMENT comment '主键',
  `node_code` varchar(32)  null default '' comment '节点编号',
  `node_name` varchar(64)  null default '' comment '节点名称',
  `node_type` varchar(32)  null default '' comment '流程节点或者审核节点',
  `node_service_type` varchar(64)  null default '' comment '节点业务类型',
  `node_detail` varchar(128)  null default '' comment '节点内部定义（目前主要是审核节点使用）',
  `status` varchar(8)  null default '' comment '状态',
  `create_user` bigint(20) unsigned null default 0 comment '创建人',
  `create_name` varchar(64)  null default '' comment '创建人名称',
  `create_time` datetime not null default current_timestamp comment '创建时间',
  `update_user` bigint(20) unsigned null default 0 comment '更新人',
  `update_name` varchar(64)  null default '' comment '更新人名称',
  `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
  primary key (`id`) using btree
) engine = innodb  comment = '流程节点表';

-- ----------------------------
-- Table structure for scf_flow_record
-- ----------------------------
drop table if exists `scf_flow_record`;
create table `scf_flow_record`  (
  `id` bigint(20) unsigned not null AUTO_INCREMENT comment '主键',
  `instants_no` varchar(64)  null default '' comment '流程实例流水号',
  `flow_code` varchar(64)  null default '' comment '节点编码',
  `flow_name` varchar(64)  null default '' comment '节点名称',
  `sept` int(10) unsigned null default 0 comment '流程步骤',
  `node_code` varchar(32)  null default '' comment '节点编号',
  `node_name` varchar(64)  null default '' comment '节点名称',
  `status` varchar(8)  null default '' comment '状态',
  `create_user` bigint(20) unsigned null default 0 comment '创建人',
  `create_name` varchar(64)  null default '' comment '创建人名称',
  `create_time` datetime not null default current_timestamp comment '创建时间',
  `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
  primary key (`id`) using btree
) engine = innodb        comment = '流程节点表';



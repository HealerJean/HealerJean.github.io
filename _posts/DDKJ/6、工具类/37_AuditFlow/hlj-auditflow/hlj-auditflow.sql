/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : d01_shiro_login

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 10/08/2019 13:42:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for scf_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `scf_sys_menu`;
CREATE TABLE `scf_sys_menu`  (
  `id` bigint(16) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ref_system_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '系统CODE',
  `menu_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '菜单名称',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '菜单调用地址',
  `method` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '调用方法（GET，POST，PUT，DELETE）',
  `status` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '状态',
  `pid` bigint(16) UNSIGNED NULL DEFAULT NULL COMMENT '父级id',
  `pchain` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '父链id，“,”分隔',
  `desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '描述',
  `sort` int(8) NULL DEFAULT NULL COMMENT '显示排序',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '菜单图标',
  `front_key` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '前端菜单标识（前端菜单唯一标识）',
  `is_permission` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '是否需要权限拦截，10：需要，99：不需要',
  `menu_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '菜单类型：0: 后端路径, 1:前端菜单，2:非展示前端菜单',
  `create_user` bigint(16) UNSIGNED NULL DEFAULT NULL COMMENT '创建人',
  `create_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(16) UNSIGNED NULL DEFAULT NULL COMMENT '更新人',
  `update_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '更新人名称',
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统模块-菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scf_sys_menu
-- ----------------------------
INSERT INTO `scf_sys_menu` VALUES (1, 'hlj-manager', '用户管理', '1', 'get', '10', 0, '0', NULL, NULL, NULL, NULL, '10', '1', NULL, NULL, '2019-06-04 19:28:14', NULL, NULL, '2019-07-09 17:24:01');
INSERT INTO `scf_sys_menu` VALUES (2, 'hlj-manager', '合同管理', '1', 'get', '10', 0, '0', NULL, NULL, NULL, NULL, '10', '1', NULL, NULL, '2019-07-09 15:25:45', NULL, NULL, '2019-07-09 15:49:19');
INSERT INTO `scf_sys_menu` VALUES (3, 'hlj-manager', '用户-修改', '/api/user/*', 'put', '10', 1, '1,0', NULL, NULL, NULL, NULL, '10', '0', NULL, NULL, '2019-06-12 17:19:08', NULL, NULL, '2019-07-09 15:29:22');
INSERT INTO `scf_sys_menu` VALUES (4, 'hlj-manager', '用户-删除', '/api/user/*', 'delete', '10', 1, '1,0', NULL, NULL, NULL, NULL, '10', '0', NULL, NULL, '2019-06-12 17:19:21', NULL, NULL, '2019-07-09 15:29:23');
INSERT INTO `scf_sys_menu` VALUES (5, 'hlj-manager', '用户-查询', '/api/user/*', 'get', '10', 1, '1,0', NULL, NULL, NULL, NULL, '10', '0', NULL, NULL, '2019-06-12 17:19:35', NULL, NULL, '2019-07-09 15:29:24');
INSERT INTO `scf_sys_menu` VALUES (6, 'hlj-manager', '用户-新增', '/api/user/add', 'post', '10', 1, '1,0', NULL, NULL, NULL, NULL, '10', '0', NULL, NULL, '2019-06-12 17:21:05', NULL, NULL, '2019-07-09 15:29:26');
INSERT INTO `scf_sys_menu` VALUES (8, 'hlj-client', '用户-查询', '/api/users', 'get', '10', 12, '12,0', NULL, NULL, NULL, NULL, '10', '0', NULL, NULL, '2019-06-23 15:45:46', NULL, NULL, '2019-07-09 15:31:24');
INSERT INTO `scf_sys_menu` VALUES (9, 'hlj-manager', '用户-列表查询', '/api/users', 'get', '10', 1, '1,0', NULL, NULL, NULL, NULL, '10', '0', NULL, NULL, '2019-06-12 17:16:04', NULL, NULL, '2019-07-09 15:31:30');
INSERT INTO `scf_sys_menu` VALUES (10, 'hlj-manager', '合同-查询', '/api/contract/*', 'get', '10', 2, '2,0', NULL, NULL, NULL, NULL, '10', '0', NULL, NULL, '2019-07-09 15:27:23', NULL, NULL, '2019-07-09 15:31:46');
INSERT INTO `scf_sys_menu` VALUES (11, 'hlj-manager', '合同-修改', '/api/contract/*', 'get', '10', 2, '2,0', NULL, NULL, NULL, NULL, '10', '0', NULL, NULL, '2019-07-09 15:27:23', NULL, NULL, '2019-07-09 15:31:47');
INSERT INTO `scf_sys_menu` VALUES (12, 'hlj-client', '个人信息', '1', 'get', '10', 0, '0', NULL, NULL, NULL, NULL, '10', '0', NULL, NULL, '2019-06-23 15:45:46', NULL, NULL, '2019-07-09 15:30:31');
INSERT INTO `scf_sys_menu` VALUES (13, 'hlj-manager', '系统管理', '1', 'get', '10', 0, '0', NULL, NULL, NULL, NULL, '10', '1', NULL, NULL, '2019-07-09 17:01:43', NULL, NULL, '2019-07-09 17:24:03');
INSERT INTO `scf_sys_menu` VALUES (14, 'hlj-manager', '系统-当前用户菜单查询', '/api/user/current/menus', 'get', '10', 15, '15,13,0', NULL, NULL, NULL, NULL, '10', '0', NULL, NULL, '2019-07-09 17:02:11', NULL, NULL, '2019-07-09 17:21:32');
INSERT INTO `scf_sys_menu` VALUES (15, 'hlj-manager', '系统菜单', '/menu', 'get', '10', 13, '13,0', NULL, NULL, NULL, NULL, '10', '1', NULL, NULL, '2019-07-09 17:21:17', NULL, NULL, '2019-07-09 17:23:58');

-- ----------------------------
-- Table structure for scf_sys_ref_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `scf_sys_ref_role_menu`;
CREATE TABLE `scf_sys_ref_role_menu`  (
  `id` bigint(16) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ref_role_id` bigint(16) UNSIGNED NOT NULL COMMENT '角色ID',
  `ref_menu_id` bigint(16) UNSIGNED NOT NULL COMMENT '菜单ID',
  `status` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '状态',
  `create_user` bigint(16) UNSIGNED NULL DEFAULT NULL COMMENT '创建人',
  `create_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(16) UNSIGNED NULL DEFAULT NULL COMMENT '更新人',
  `update_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '更新人名称',
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_id`(`ref_role_id`) USING BTREE COMMENT '角色id索引'
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统模块-角色与菜单关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scf_sys_ref_role_menu
-- ----------------------------
INSERT INTO `scf_sys_ref_role_menu` VALUES (1, 1, 1, '10', NULL, NULL, '2019-06-13 15:53:50', NULL, NULL, '2019-06-23 15:28:52');
INSERT INTO `scf_sys_ref_role_menu` VALUES (2, 1, 2, '10', NULL, NULL, '2019-06-13 15:53:58', NULL, NULL, NULL);
INSERT INTO `scf_sys_ref_role_menu` VALUES (3, 1, 3, '10', NULL, NULL, '2019-06-13 15:54:10', NULL, NULL, NULL);
INSERT INTO `scf_sys_ref_role_menu` VALUES (4, 1, 4, '10', NULL, NULL, '2019-06-13 15:54:21', NULL, NULL, NULL);
INSERT INTO `scf_sys_ref_role_menu` VALUES (5, 1, 5, '10', NULL, NULL, '2019-06-13 15:54:29', NULL, NULL, NULL);
INSERT INTO `scf_sys_ref_role_menu` VALUES (6, 1, 6, '10', NULL, NULL, '2019-06-13 15:54:35', NULL, NULL, NULL);
INSERT INTO `scf_sys_ref_role_menu` VALUES (7, 1, 9, '10', NULL, NULL, '2019-06-23 15:46:04', NULL, NULL, '2019-07-09 15:32:16');
INSERT INTO `scf_sys_ref_role_menu` VALUES (8, 1, 10, '10', NULL, NULL, '2019-07-09 15:24:45', NULL, NULL, '2019-07-09 15:32:23');
INSERT INTO `scf_sys_ref_role_menu` VALUES (9, 1, 11, '10', NULL, NULL, '2019-07-09 15:24:54', NULL, NULL, '2019-07-09 15:32:24');
INSERT INTO `scf_sys_ref_role_menu` VALUES (10, 2, 9, '10', NULL, NULL, '2019-07-09 15:32:42', NULL, NULL, NULL);
INSERT INTO `scf_sys_ref_role_menu` VALUES (11, 2, 12, '10', NULL, NULL, '2019-07-09 15:32:49', NULL, NULL, '2019-07-09 15:33:03');
INSERT INTO `scf_sys_ref_role_menu` VALUES (12, 3, 2, '10', NULL, NULL, '2019-07-09 15:33:25', NULL, NULL, NULL);
INSERT INTO `scf_sys_ref_role_menu` VALUES (13, 3, 10, '10', NULL, NULL, '2019-07-09 15:33:29', NULL, NULL, '2019-07-09 15:33:42');
INSERT INTO `scf_sys_ref_role_menu` VALUES (14, 3, 11, '10', NULL, NULL, '2019-07-09 15:33:47', NULL, NULL, NULL);
INSERT INTO `scf_sys_ref_role_menu` VALUES (15, 1, 13, '10', NULL, NULL, '2019-07-09 17:02:43', NULL, NULL, '2019-07-09 17:02:59');
INSERT INTO `scf_sys_ref_role_menu` VALUES (16, 1, 14, '10', NULL, NULL, '2019-07-09 17:02:48', NULL, NULL, '2019-07-09 17:03:00');
INSERT INTO `scf_sys_ref_role_menu` VALUES (17, 1, 15, '10', NULL, NULL, '2019-07-09 17:22:11', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for scf_sys_ref_user_role
-- ----------------------------
DROP TABLE IF EXISTS `scf_sys_ref_user_role`;
CREATE TABLE `scf_sys_ref_user_role`  (
  `id` bigint(16) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ref_user_id` bigint(16) UNSIGNED NOT NULL COMMENT '用户ID',
  `ref_role_id` bigint(16) UNSIGNED NOT NULL COMMENT '角色ID',
  `status` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '状态',
  `create_user` bigint(16) UNSIGNED NULL DEFAULT NULL COMMENT '创建人',
  `create_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(16) UNSIGNED NULL DEFAULT NULL COMMENT '更新人',
  `update_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '更新人名称',
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`ref_user_id`) USING BTREE COMMENT '用户id索引'
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统模块-用户与角色关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scf_sys_ref_user_role
-- ----------------------------
INSERT INTO `scf_sys_ref_user_role` VALUES (1, 1, 1, '10', 1, 'HealerJean', '2019-06-13 15:43:02', 1, 'HealerJean', NULL);
INSERT INTO `scf_sys_ref_user_role` VALUES (2, 2, 3, '10', NULL, NULL, '2019-07-09 15:34:05', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for scf_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `scf_sys_role`;
CREATE TABLE `scf_sys_role`  (
  `id` bigint(16) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '角色名称',
  `ref_system_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '系统CODE',
  `status` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '状态',
  `desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '描述',
  `create_user` bigint(16) UNSIGNED NULL DEFAULT NULL COMMENT '创建人',
  `create_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(16) UNSIGNED NULL DEFAULT NULL COMMENT '更新人',
  `update_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '更新人名称',
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统模块-角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scf_sys_role
-- ----------------------------
INSERT INTO `scf_sys_role` VALUES (1, '系统管理员', 'hlj-manager', '10', '系统管理员', 1, 'HealerJean', '2019-06-13 15:43:42', 1, 'HealerJean', '2019-06-23 15:40:19');
INSERT INTO `scf_sys_role` VALUES (2, '前台用户', 'hlj-client', '10', '前台用户', 1, NULL, '2019-07-05 16:35:19', NULL, NULL, NULL);
INSERT INTO `scf_sys_role` VALUES (3, '运营用户', 'hlj-manager', '10', '系统的运营人员', NULL, NULL, '2019-07-09 15:15:21', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for scf_user_department
-- ----------------------------
DROP TABLE IF EXISTS `scf_user_department`;
CREATE TABLE `scf_user_department`  (
  `id` bigint(16) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `department_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '部门名称',
  `department_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '部门描述',
  `pid` bigint(16) UNSIGNED NOT NULL DEFAULT 0 COMMENT '主键',
  `status` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '状态',
  `create_user` bigint(16) UNSIGNED NULL DEFAULT NULL COMMENT '创建人',
  `create_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(16) UNSIGNED NULL DEFAULT NULL COMMENT '更新人',
  `update_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '更新人名称',
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统模块-部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scf_user_department
-- ----------------------------
INSERT INTO `scf_user_department` VALUES (1, '平台部', '最高指挥部', 0, '10', NULL, NULL, '2019-06-23 15:30:33', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for scf_user_info
-- ----------------------------
DROP TABLE IF EXISTS `scf_user_info`;
CREATE TABLE `scf_user_info`  (
  `id` bigint(19) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `real_name` varchar(64) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '用户名',
  `username` varchar(64) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '真实姓名',
  `password` varchar(128) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '密码',
  `email` varchar(64) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '邮箱',
  `telephone` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '手机号',
  `gender` varchar(8) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '性别',
  `user_type` varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '用户类型',
  `status` varchar(8) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '用户状态',
  `create_user` bigint(16) UNSIGNED NULL DEFAULT NULL COMMENT '创建人',
  `create_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(16) UNSIGNED NULL DEFAULT NULL COMMENT '更新人',
  `update_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '更新人名称',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `new_column` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scf_user_info
-- ----------------------------
INSERT INTO `scf_user_info` VALUES (1, 'healerjean', 'HealerJean', 'admin', 'healerjean@gmail.com', '18888888888', 'gender', 'admin', '10', NULL, NULL, '2019-06-23 14:30:12', NULL, NULL, '2019-07-05 13:48:48', NULL);
INSERT INTO `scf_user_info` VALUES (2, 'operatorName', 'operator', 'admin', 'operate@gmail.com', '17777777777', 'gender', 'operator', '10', NULL, NULL, '2019-07-09 15:35:29', NULL, NULL, '2019-07-09 15:35:53', NULL);

-- ----------------------------
-- Table structure for scf_user_ref_user_department
-- ----------------------------
DROP TABLE IF EXISTS `scf_user_ref_user_department`;
CREATE TABLE `scf_user_ref_user_department`  (
  `id` bigint(16) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ref_user_id` bigint(16) UNSIGNED NOT NULL DEFAULT 0 COMMENT '用户主键',
  `ref_department_id` bigint(16) UNSIGNED NOT NULL DEFAULT 0 COMMENT '部门主键',
  `status` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否有效10有效，99废弃',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户模块-用户-部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scf_user_ref_user_department
-- ----------------------------
INSERT INTO `scf_user_ref_user_department` VALUES (1, 1, 1, '10');

SET FOREIGN_KEY_CHECKS = 1;



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




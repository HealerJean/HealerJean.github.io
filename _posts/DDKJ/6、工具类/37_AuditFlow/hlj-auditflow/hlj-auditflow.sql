/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : hlj-auditflow

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 13/08/2019 20:09:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for flow_audit_record_log
-- ----------------------------
DROP TABLE IF EXISTS `flow_audit_record_log`;
CREATE TABLE `flow_audit_record_log`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ref_file_ids` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '审批附件',
  `instants_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '流程实例流水号',
  `sept` int(10) UNSIGNED NULL DEFAULT 0 COMMENT '流程步骤',
  `flow_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '节点编码',
  `flow_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '节点名称',
  `node_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '节点编号',
  `node_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '节点名称',
  `audit_sept` int(10) UNSIGNED NULL DEFAULT 0 COMMENT '审核步骤',
  `audit_data` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '审批内容',
  `status` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '状态',
  `audit_message` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '审批意见',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `ref_flow_audit_record_id` bigint(20) UNSIGNED NOT NULL COMMENT '审批记录表主键',
  `create_user` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '创建人',
  `create_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建人名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '审批日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flow_audit_record_log
-- ----------------------------
INSERT INTO `flow_audit_record_log` VALUES (1, '', '7370be6d9560427bafde01d43e5eb477', 3, 'auditJob', '审核任务', 'auditAJobDeal', '审核任务A处理', 1, '{\"instanceNo\":\"7370be6d9560427bafde01d43e5eb477\",\"nextFlow\":true}', '20', '', '2019-08-13 19:06:33', '2019-08-13 19:06:33', 1, 1, 'healerjean');
INSERT INTO `flow_audit_record_log` VALUES (2, '', '7370be6d9560427bafde01d43e5eb477', 3, 'auditJob', '审核任务', 'auditAJobDeal', '审核任务A处理', 1, '{\"instanceNo\":\"7370be6d9560427bafde01d43e5eb477\",\"nextFlow\":true}', '10', '', '2019-08-13 19:06:47', '2019-08-13 19:06:47', 1, 1, 'healerjean');
INSERT INTO `flow_audit_record_log` VALUES (3, '', '7370be6d9560427bafde01d43e5eb477', 5, 'auditJob', '审核任务', 'auditBJobDeal', '审核任务B处理', 1, '{\"instanceNo\":\"7370be6d9560427bafde01d43e5eb477\",\"nextFlow\":true}', '20', '', '2019-08-13 19:06:47', '2019-08-13 19:06:47', 2, 1, 'healerjean');
INSERT INTO `flow_audit_record_log` VALUES (4, '', '7370be6d9560427bafde01d43e5eb477', 5, 'auditJob', '审核任务', 'auditBJobDeal', '审核任务B处理', 1, '{\"instanceNo\":\"7370be6d9560427bafde01d43e5eb477\",\"nextFlow\":true}', '10', '', '2019-08-13 19:06:56', '2019-08-13 19:06:56', 2, 1, 'healerjean');
INSERT INTO `flow_audit_record_log` VALUES (5, '', '7370be6d9560427bafde01d43e5eb477', 5, 'auditJob', '审核任务', 'auditBJobDeal', '审核任务B处理', 2, '{\"instanceNo\":\"7370be6d9560427bafde01d43e5eb477\",\"nextFlow\":true}', '20', '', '2019-08-13 19:06:56', '2019-08-13 19:06:56', 3, 1, 'healerjean');
INSERT INTO `flow_audit_record_log` VALUES (6, '', '7370be6d9560427bafde01d43e5eb477', 5, 'auditJob', '审核任务', 'auditBJobDeal', '审核任务B处理', 2, '{\"instanceNo\":\"7370be6d9560427bafde01d43e5eb477\",\"nextFlow\":true}', '10', '', '2019-08-13 19:07:24', '2019-08-13 19:07:24', 3, 2, 'operatorName');
INSERT INTO `flow_audit_record_log` VALUES (11, '', 'b6ad5dacf2454c45a4e1abff660f57cc', 3, 'auditJob', '审核任务', 'auditAJobDeal', '审核任务A处理', 1, '{\"instanceNo\":\"b6ad5dacf2454c45a4e1abff660f57cc\",\"nextFlow\":true}', '20', '', '2019-08-13 20:01:24', '2019-08-13 20:01:24', 4, 1, 'healerjean');
INSERT INTO `flow_audit_record_log` VALUES (12, '', 'b6ad5dacf2454c45a4e1abff660f57cc', 3, 'auditJob', '审核任务', 'auditAJobDeal', '审核任务A处理', 1, '{\"instanceNo\":\"b6ad5dacf2454c45a4e1abff660f57cc\",\"nextFlow\":true}', '10', '', '2019-08-13 20:04:43', '2019-08-13 20:04:43', 4, 1, 'healerjean');
INSERT INTO `flow_audit_record_log` VALUES (13, '', 'b6ad5dacf2454c45a4e1abff660f57cc', 5, 'auditJob', '审核任务', 'auditBJobDeal', '审核任务B处理', 1, '{\"instanceNo\":\"b6ad5dacf2454c45a4e1abff660f57cc\",\"nextFlow\":true}', '20', '', '2019-08-13 20:05:32', '2019-08-13 20:05:32', 5, 1, 'healerjean');
INSERT INTO `flow_audit_record_log` VALUES (14, '', 'b6ad5dacf2454c45a4e1abff660f57cc', 5, 'auditJob', '审核任务', 'auditBJobDeal', '审核任务B处理', 1, '{\"instanceNo\":\"b6ad5dacf2454c45a4e1abff660f57cc\",\"nextFlow\":true}', '10', '', '2019-08-13 20:06:16', '2019-08-13 20:06:16', 5, 1, 'healerjean');
INSERT INTO `flow_audit_record_log` VALUES (15, '', 'b6ad5dacf2454c45a4e1abff660f57cc', 5, 'auditJob', '审核任务', 'auditBJobDeal', '审核任务B处理', 2, '{\"instanceNo\":\"b6ad5dacf2454c45a4e1abff660f57cc\",\"nextFlow\":true}', '20', '', '2019-08-13 20:06:16', '2019-08-13 20:06:16', 6, 1, 'healerjean');
INSERT INTO `flow_audit_record_log` VALUES (17, '', 'b6ad5dacf2454c45a4e1abff660f57cc', 5, 'auditJob', '审核任务', 'auditBJobDeal', '审核任务B处理', 2, '{\"instanceNo\":\"b6ad5dacf2454c45a4e1abff660f57cc\",\"nextFlow\":true}', '10', '', '2019-08-13 20:08:39', '2019-08-13 20:08:39', 6, 2, 'operatorName');

-- ----------------------------
-- Table structure for flow_ref_auditor_event
-- ----------------------------
DROP TABLE IF EXISTS `flow_ref_auditor_event`;
CREATE TABLE `flow_ref_auditor_event`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ref_flow_audit_record_id` bigint(20) UNSIGNED NOT NULL COMMENT '审批记录表主键',
  `audit_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '审核类型：角色或ID',
  `audit_object` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '审核对象',
  `copy` tinyint(1) NOT NULL COMMENT '是否是抄送人 1 表示是，默认为0',
  `create_user` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '创建人',
  `create_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '审批人和审批事件关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flow_ref_auditor_event
-- ----------------------------
INSERT INTO `flow_ref_auditor_event` VALUES (1, 1, 'ID', 1, 0, 0, '', '2019-08-13 19:06:33', '2019-08-13 19:06:33');
INSERT INTO `flow_ref_auditor_event` VALUES (2, 1, 'ID', 2, 0, 0, '', '2019-08-13 19:06:33', '2019-08-13 19:06:33');
INSERT INTO `flow_ref_auditor_event` VALUES (3, 1, 'ROLE', 1, 0, 0, '', '2019-08-13 19:06:33', '2019-08-13 19:06:33');
INSERT INTO `flow_ref_auditor_event` VALUES (4, 1, 'ROLE', 2, 0, 0, '', '2019-08-13 19:06:33', '2019-08-13 19:06:33');
INSERT INTO `flow_ref_auditor_event` VALUES (5, 2, 'ID', 1, 0, 0, '', '2019-08-13 19:06:47', '2019-08-13 19:06:47');
INSERT INTO `flow_ref_auditor_event` VALUES (6, 2, 'ROLE', 1, 0, 0, '', '2019-08-13 19:06:47', '2019-08-13 19:06:47');
INSERT INTO `flow_ref_auditor_event` VALUES (7, 3, 'ID', 2, 0, 0, '', '2019-08-13 19:06:56', '2019-08-13 19:06:56');
INSERT INTO `flow_ref_auditor_event` VALUES (8, 3, 'ROLE', 2, 0, 0, '', '2019-08-13 19:06:56', '2019-08-13 19:06:56');
INSERT INTO `flow_ref_auditor_event` VALUES (9, 4, 'ID', 1, 0, 0, '', '2019-08-13 20:01:24', '2019-08-13 20:01:24');
INSERT INTO `flow_ref_auditor_event` VALUES (10, 4, 'ID', 2, 0, 0, '', '2019-08-13 20:01:25', '2019-08-13 20:01:25');
INSERT INTO `flow_ref_auditor_event` VALUES (11, 4, 'ROLE', 1, 0, 0, '', '2019-08-13 20:01:25', '2019-08-13 20:01:25');
INSERT INTO `flow_ref_auditor_event` VALUES (12, 4, 'ROLE', 2, 0, 0, '', '2019-08-13 20:01:25', '2019-08-13 20:01:25');
INSERT INTO `flow_ref_auditor_event` VALUES (13, 4, 'ID', 1, 1, 0, '', '2019-08-13 20:05:30', '2019-08-13 20:05:30');
INSERT INTO `flow_ref_auditor_event` VALUES (14, 4, 'ID', 2, 1, 0, '', '2019-08-13 20:05:30', '2019-08-13 20:05:30');
INSERT INTO `flow_ref_auditor_event` VALUES (15, 4, 'ROLE', 1, 1, 0, '', '2019-08-13 20:05:30', '2019-08-13 20:05:30');
INSERT INTO `flow_ref_auditor_event` VALUES (16, 4, 'ROLE', 2, 1, 0, '', '2019-08-13 20:05:30', '2019-08-13 20:05:30');
INSERT INTO `flow_ref_auditor_event` VALUES (17, 5, 'ID', 1, 0, 0, '', '2019-08-13 20:05:32', '2019-08-13 20:05:32');
INSERT INTO `flow_ref_auditor_event` VALUES (18, 5, 'ROLE', 1, 0, 0, '', '2019-08-13 20:05:32', '2019-08-13 20:05:32');
INSERT INTO `flow_ref_auditor_event` VALUES (19, 6, 'ID', 2, 0, 0, '', '2019-08-13 20:06:16', '2019-08-13 20:06:16');
INSERT INTO `flow_ref_auditor_event` VALUES (20, 6, 'ROLE', 2, 0, 0, '', '2019-08-13 20:06:16', '2019-08-13 20:06:16');

-- ----------------------------
-- Table structure for scf_flow_audit_record
-- ----------------------------
DROP TABLE IF EXISTS `scf_flow_audit_record`;
CREATE TABLE `scf_flow_audit_record`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ref_file_ids` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '审批附件',
  `instants_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '流程实例流水号',
  `sept` int(10) UNSIGNED NULL DEFAULT 0 COMMENT '流程步骤',
  `flow_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '节点编码',
  `flow_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '节点名称',
  `node_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '节点编号',
  `node_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '节点名称',
  `audit_sept` int(10) UNSIGNED NULL DEFAULT 0 COMMENT '审核步骤',
  `audit_data` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '审批内容',
  `status` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '状态',
  `opt_user` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '执行人',
  `opt_user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '执行人真实名称',
  `opt_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '执行时间',
  `audit_message` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '审批意见',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `create_user` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '创建人',
  `create_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建人名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '审核记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scf_flow_audit_record
-- ----------------------------
INSERT INTO `scf_flow_audit_record` VALUES (1, '', '7370be6d9560427bafde01d43e5eb477', 3, 'auditJob', '审核任务', 'auditAJobDeal', '审核任务A处理', 1, '{\"instanceNo\":\"7370be6d9560427bafde01d43e5eb477\",\"nextFlow\":true}', '10', 1, 'healerjean', '2019-08-13 19:06:48', '', '2019-08-13 19:06:33', '2019-08-13 19:06:47', 1, 'healerjean');
INSERT INTO `scf_flow_audit_record` VALUES (2, '', '7370be6d9560427bafde01d43e5eb477', 5, 'auditJob', '审核任务', 'auditBJobDeal', '审核任务B处理', 1, '{\"instanceNo\":\"7370be6d9560427bafde01d43e5eb477\",\"nextFlow\":true}', '10', 1, 'healerjean', '2019-08-13 19:06:57', '', '2019-08-13 19:06:47', '2019-08-13 19:06:56', 1, 'healerjean');
INSERT INTO `scf_flow_audit_record` VALUES (3, '', '7370be6d9560427bafde01d43e5eb477', 5, 'auditJob', '审核任务', 'auditBJobDeal', '审核任务B处理', 2, '{\"instanceNo\":\"7370be6d9560427bafde01d43e5eb477\",\"nextFlow\":true}', '10', 2, 'operatorName', '2019-08-13 19:07:25', '', '2019-08-13 19:06:56', '2019-08-13 19:07:24', 1, 'healerjean');
INSERT INTO `scf_flow_audit_record` VALUES (4, '', 'b6ad5dacf2454c45a4e1abff660f57cc', 3, 'auditJob', '审核任务', 'auditAJobDeal', '审核任务A处理', 1, '{\"instanceNo\":\"b6ad5dacf2454c45a4e1abff660f57cc\",\"nextFlow\":true}', '10', 1, 'healerjean', '2019-08-13 20:04:44', '', '2019-08-13 20:01:24', '2019-08-13 20:04:43', 1, 'healerjean');
INSERT INTO `scf_flow_audit_record` VALUES (5, '', 'b6ad5dacf2454c45a4e1abff660f57cc', 5, 'auditJob', '审核任务', 'auditBJobDeal', '审核任务B处理', 1, '{\"instanceNo\":\"b6ad5dacf2454c45a4e1abff660f57cc\",\"nextFlow\":true}', '10', 1, 'healerjean', '2019-08-13 20:06:16', '', '2019-08-13 20:05:32', '2019-08-13 20:06:16', 1, 'healerjean');
INSERT INTO `scf_flow_audit_record` VALUES (6, '', 'b6ad5dacf2454c45a4e1abff660f57cc', 5, 'auditJob', '审核任务', 'auditBJobDeal', '审核任务B处理', 2, '{\"instanceNo\":\"b6ad5dacf2454c45a4e1abff660f57cc\",\"nextFlow\":true}', '10', 2, 'operatorName', '2019-08-13 20:08:39', '', '2019-08-13 20:06:16', '2019-08-13 20:08:39', 1, 'healerjean');

-- ----------------------------
-- Table structure for scf_flow_definition
-- ----------------------------
DROP TABLE IF EXISTS `scf_flow_definition`;
CREATE TABLE `scf_flow_definition`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `flow_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '节点编码',
  `flow_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '节点名称',
  `flow_definition` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '节点内部定义（目前主要是审核节点使用）',
  `status` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '状态',
  `create_user` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '创建人',
  `create_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '更新人',
  `update_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新人名称',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '流程定义表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scf_flow_definition
-- ----------------------------
INSERT INTO `scf_flow_definition` VALUES (1, 'demoJob', 'Demo任务', '[\"demoJobSubmit\",\"demoAJobDeal\",\"demoBJobDeal\",\"demoCJobDeal\",\"demoJobsuccess\"]', '10', 0, '', '2019-08-11 17:54:45', 0, '', '2019-08-11 18:11:07');
INSERT INTO `scf_flow_definition` VALUES (2, 'auditJob', '审核任务', '[\"auditJobSubmit\",\"auditServiceAJobDeal\",\"auditAJobDeal\",\"auditServiceBJobDeal\",\"auditBJobDeal\",\"auditJobsuccess\"]', '10', 0, '', '2019-08-11 19:07:24', 0, '', '2019-08-11 19:39:29');

-- ----------------------------
-- Table structure for scf_flow_node
-- ----------------------------
drop table if exists `scf_flow_node`;
create table `scf_flow_node` (
  `id` bigint(20) unsigned not null auto_increment comment '主键',
  `node_code` varchar(32) default '' comment '节点编号,当时审批业务的时候，可以使用代码生成无需的',
  `node_name` varchar(64) default '' comment '节点名称',
  `node_type` varchar(32) default '' comment '流程节点或者审核节点',
  `audit_business_type` varchar(32) default null comment '审批业务业务类型,主要目的为用于区分自定添加审批人的业务属于同一种类',
  `auditors` varchar(128) default '' comment '节点内部定义（目前主要是审核节点使用）',
  `copy_to` varchar(128) default null comment '抄送人',
  `status` varchar(8) default '' comment '状态',
  `create_user` bigint(20) unsigned default '0' comment '创建人',
  `create_name` varchar(64) default '' comment '创建人名称',
  `create_time` datetime not null default current_timestamp comment '创建时间',
  `update_user` bigint(20) unsigned default '0' comment '更新人',
  `update_name` varchar(64) default '' comment '更新人名称',
  `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
  primary key (`id`) using btree
) engine=innodb auto_increment=13 default charset=utf8 row_format=dynamic comment='流程节点表';


-- ----------------------------
-- Records of scf_flow_node
-- ----------------------------
INSERT INTO `scf_flow_node` VALUES (1, 'demoJobSubmit', '任务提交', 'ServiceNode', '', NULL, '10', 0, '', '2019-08-11 17:50:52', 0, '', '2019-08-11 17:50:52');
INSERT INTO `scf_flow_node` VALUES (2, 'demoAJobDeal', '任务A处理', 'ServiceNode', '', NULL, '10', 0, '', '2019-08-11 17:51:12', 0, '', '2019-08-11 17:51:12');
INSERT INTO `scf_flow_node` VALUES (3, 'demoBJobDeal', '任务B处理', 'ServiceNode', '', NULL, '10', 0, '', '2019-08-11 17:51:26', 0, '', '2019-08-11 17:51:26');
INSERT INTO `scf_flow_node` VALUES (4, 'demoCJobDeal', '任务C处理', 'ServiceNode', '', NULL, '10', 0, '', '2019-08-11 17:51:53', 0, '', '2019-08-11 17:51:53');
INSERT INTO `scf_flow_node` VALUES (5, 'demoJobsuccess', '任务完成', 'ServiceNode', '', NULL, '10', 0, '', '2019-08-11 17:52:06', 0, '', '2019-08-11 17:52:06');
INSERT INTO `scf_flow_node` VALUES (6, 'auditJobSubmit', '审核任务提交', 'ServiceNode', '', NULL, '10', 0, '', '2019-08-11 18:49:28', 0, '', '2019-08-11 18:49:28');
INSERT INTO `scf_flow_node` VALUES (7, 'auditServiceAJobDeal', '审核任务ServiceA处理', 'ServiceNode', '', NULL, '10', 0, '', '2019-08-11 18:49:44', 0, '', '2019-08-11 18:49:58');
INSERT INTO `scf_flow_node` VALUES (8, 'auditAJobDeal', '审核任务A处理', 'AuditNode', '[{\"roles\" : [1,2], \"ids\":[1,2]}]', '{\"roles\" : [1,2], \"ids\":[1,2]}', '10', 0, '', '2019-08-11 18:50:11', 0, '', '2019-08-13 19:45:27');
INSERT INTO `scf_flow_node` VALUES (9, 'auditServiceBJobDeal', '审核任务ServiceB处理', 'ServiceNode', '', NULL, '10', 0, '', '2019-08-11 18:50:27', 0, '', '2019-08-11 18:53:13');
INSERT INTO `scf_flow_node` VALUES (10, 'auditBJobDeal', '审核任务B处理', 'AuditNode', '[{\"roles\" : [1], \"ids\":[1]},{\"roles\" : [2], \"ids\":[2]}]', NULL, '10', 0, '', '2019-08-11 18:50:35', 0, '', '2019-08-13 18:48:13');
INSERT INTO `scf_flow_node` VALUES (11, 'auditServiceBJobDeal', '审核任务ServiceB处理', 'ServiceNode', '', NULL, '10', 0, '', '2019-08-11 18:50:57', 0, '', '2019-08-11 18:53:17');
INSERT INTO `scf_flow_node` VALUES (12, 'auditJobsuccess', '审核任务完成', 'ServiceNode', '', NULL, '10', 0, '', '2019-08-11 18:51:22', 0, '', '2019-08-11 18:53:19');

-- ----------------------------
-- Table structure for scf_flow_record
-- ----------------------------
DROP TABLE IF EXISTS `scf_flow_record`;
CREATE TABLE `scf_flow_record`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `instants_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '流程实例流水号',
  `flow_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '节点编码',
  `flow_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '节点名称',
  `sept` int(10) UNSIGNED NULL DEFAULT 0 COMMENT '流程步骤',
  `node_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '节点编号',
  `node_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '节点名称',
  `status` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '状态',
  `create_user` bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '创建人',
  `create_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '流程节点表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scf_flow_record
-- ----------------------------
INSERT INTO `scf_flow_record` VALUES (1, '2e6cd0959dfb4b62bee27232b1f42ad8', 'demoJob', 'Demo任务', 1, 'demoJobSubmit', '任务提交', '10', 1, 'healerjean', '2019-08-13 19:06:02', '2019-08-13 19:06:02');
INSERT INTO `scf_flow_record` VALUES (2, '2e6cd0959dfb4b62bee27232b1f42ad8', 'demoJob', 'Demo任务', 2, 'demoAJobDeal', '任务A处理', '10', 1, 'healerjean', '2019-08-13 19:06:02', '2019-08-13 19:06:02');
INSERT INTO `scf_flow_record` VALUES (3, '2e6cd0959dfb4b62bee27232b1f42ad8', 'demoJob', 'Demo任务', 3, 'demoBJobDeal', '任务B处理', '10', 1, 'healerjean', '2019-08-13 19:06:02', '2019-08-13 19:06:14');
INSERT INTO `scf_flow_record` VALUES (4, '2e6cd0959dfb4b62bee27232b1f42ad8', 'demoJob', 'Demo任务', 4, 'demoCJobDeal', '任务C处理', '10', 1, 'healerjean', '2019-08-13 19:06:14', '2019-08-13 19:06:14');
INSERT INTO `scf_flow_record` VALUES (5, '2e6cd0959dfb4b62bee27232b1f42ad8', 'demoJob', 'Demo任务', 5, 'demoJobsuccess', '任务完成', '10', 1, 'healerjean', '2019-08-13 19:06:14', '2019-08-13 19:06:14');
INSERT INTO `scf_flow_record` VALUES (6, '7370be6d9560427bafde01d43e5eb477', 'auditJob', '审核任务', 1, 'auditJobSubmit', '审核任务提交', '10', 1, 'healerjean', '2019-08-13 19:06:22', '2019-08-13 19:06:22');
INSERT INTO `scf_flow_record` VALUES (7, '7370be6d9560427bafde01d43e5eb477', 'auditJob', '审核任务', 2, 'auditServiceAJobDeal', '审核任务ServiceA处理', '10', 1, 'healerjean', '2019-08-13 19:06:22', '2019-08-13 19:06:33');
INSERT INTO `scf_flow_record` VALUES (8, '7370be6d9560427bafde01d43e5eb477', 'auditJob', '审核任务', 3, 'auditAJobDeal', '审核任务A处理', '10', 1, 'healerjean', '2019-08-13 19:06:33', '2019-08-13 19:06:47');
INSERT INTO `scf_flow_record` VALUES (9, '7370be6d9560427bafde01d43e5eb477', 'auditJob', '审核任务', 4, 'auditServiceBJobDeal', '审核任务ServiceB处理', '10', 1, 'healerjean', '2019-08-13 19:06:47', '2019-08-13 19:06:47');
INSERT INTO `scf_flow_record` VALUES (10, '7370be6d9560427bafde01d43e5eb477', 'auditJob', '审核任务', 5, 'auditBJobDeal', '审核任务B处理', '10', 1, 'healerjean', '2019-08-13 19:06:47', '2019-08-13 19:07:24');
INSERT INTO `scf_flow_record` VALUES (11, '7370be6d9560427bafde01d43e5eb477', 'auditJob', '审核任务', 6, 'auditJobsuccess', '审核任务完成', '10', 1, 'healerjean', '2019-08-13 19:07:24', '2019-08-13 19:07:24');
INSERT INTO `scf_flow_record` VALUES (12, 'b6ad5dacf2454c45a4e1abff660f57cc', 'auditJob', '审核任务', 1, 'auditJobSubmit', '审核任务提交', '10', 1, 'healerjean', '2019-08-13 20:00:51', '2019-08-13 20:00:51');
INSERT INTO `scf_flow_record` VALUES (13, 'b6ad5dacf2454c45a4e1abff660f57cc', 'auditJob', '审核任务', 2, 'auditServiceAJobDeal', '审核任务ServiceA处理', '10', 1, 'healerjean', '2019-08-13 20:00:51', '2019-08-13 20:01:24');
INSERT INTO `scf_flow_record` VALUES (14, 'b6ad5dacf2454c45a4e1abff660f57cc', 'auditJob', '审核任务', 3, 'auditAJobDeal', '审核任务A处理', '10', 1, 'healerjean', '2019-08-13 20:01:24', '2019-08-13 20:05:32');
INSERT INTO `scf_flow_record` VALUES (15, 'b6ad5dacf2454c45a4e1abff660f57cc', 'auditJob', '审核任务', 4, 'auditServiceBJobDeal', '审核任务ServiceB处理', '10', 1, 'healerjean', '2019-08-13 20:05:32', '2019-08-13 20:05:32');
INSERT INTO `scf_flow_record` VALUES (16, 'b6ad5dacf2454c45a4e1abff660f57cc', 'auditJob', '审核任务', 5, 'auditBJobDeal', '审核任务B处理', '10', 1, 'healerjean', '2019-08-13 20:05:32', '2019-08-13 20:08:42');
INSERT INTO `scf_flow_record` VALUES (17, 'b6ad5dacf2454c45a4e1abff660f57cc', 'auditJob', '审核任务', 6, 'auditJobsuccess', '审核任务完成', '10', 1, 'healerjean', '2019-08-13 20:08:42', '2019-08-13 20:08:42');

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
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统模块-菜单表' ROW_FORMAT = Dynamic;

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
INSERT INTO `scf_sys_menu` VALUES (16, 'hlj-manager', '审批任务', '/api/flow/*', 'get', '10', 0, '0', NULL, NULL, NULL, NULL, '10', '0', NULL, NULL, '2019-08-11 17:55:43', NULL, NULL, NULL);
INSERT INTO `scf_sys_menu` VALUES (17, 'hlj-manager', '审批', '/api/flow/audit', 'post', '10', 0, '0', NULL, NULL, NULL, NULL, '10', '0', NULL, NULL, '2019-08-12 18:45:06', NULL, NULL, NULL);

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
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统模块-角色与菜单关系表' ROW_FORMAT = Dynamic;

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
INSERT INTO `scf_sys_ref_role_menu` VALUES (18, 1, 16, '10', NULL, NULL, '2019-08-11 17:55:56', NULL, NULL, NULL);
INSERT INTO `scf_sys_ref_role_menu` VALUES (19, 1, 17, '10', NULL, NULL, '2019-08-12 18:45:18', NULL, NULL, NULL);
INSERT INTO `scf_sys_ref_role_menu` VALUES (20, 3, 17, '10', NULL, NULL, '2019-08-13 13:29:36', NULL, NULL, '2019-08-13 13:30:37');

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

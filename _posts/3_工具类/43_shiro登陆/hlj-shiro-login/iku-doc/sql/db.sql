/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : hlj-shiro-login

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 01/11/2019 17:53:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_department
-- ----------------------------
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '部门名称',
  `description` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '部门描述',
  `pid` bigint(20) NOT NULL DEFAULT 0 COMMENT '父部门',
  `status` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '状态: 10：有效，99：无效',
  `create_user` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人id',
  `create_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新人',
  `update_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统模块-部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_department
-- ----------------------------
INSERT INTO `sys_department` VALUES (1, '管理部', '最高级别部门', 0, '10', 0, '', '2019-11-01 11:31:32', 0, '', '2019-11-01 11:35:23');
INSERT INTO `sys_department` VALUES (2, '研发部', '开发部门', 1, '10', 0, '', '2019-11-01 11:35:38', 0, '', '2019-11-01 11:35:38');
INSERT INTO `sys_department` VALUES (3, '运营部', '运营', 1, '10', 0, '', '2019-11-01 11:36:00', 0, '', '2019-11-01 17:50:48');
INSERT INTO `sys_department` VALUES (4, '审核部', '负责审批等', 1, '10', 0, '', '2019-11-01 13:35:53', 0, '', '2019-11-01 17:50:44');
INSERT INTO `sys_department` VALUES (5, '测试部', '测试人员', 1, '10', 0, '', '2019-11-01 17:50:05', 0, '', '2019-11-01 17:50:10');

-- ----------------------------
-- Table structure for sys_dictionary_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictionary_data`;
CREATE TABLE `sys_dictionary_data`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `data_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据类型type_key  表dictionary_type',
  `data_value` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典数据 描述',
  `ref_type_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据类型type_key  表dictionary_type',
  `sort` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序',
  `status` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `create_user` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新人',
  `update_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_typekey_datakey`(`ref_type_key`, `data_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统模块-字典表数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dictionary_data
-- ----------------------------
INSERT INTO `sys_dictionary_data` VALUES (1, '10', '有效', 'Status', 0, '10', 1, '系统管理员', '2019-11-01 15:59:29', 1, '系统管理员', '2019-11-01 15:59:29');
INSERT INTO `sys_dictionary_data` VALUES (2, '99', '废弃', 'Status', 0, '10', 1, '系统管理员', '2019-11-01 15:59:40', 1, '系统管理员', '2019-11-01 15:59:40');

-- ----------------------------
-- Table structure for sys_dictionary_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictionary_type`;
CREATE TABLE `sys_dictionary_type`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型',
  `type_desc` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型 描述',
  `status` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `create_user` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新人',
  `update_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_typekey`(`type_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统模块-字典表类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dictionary_type
-- ----------------------------
INSERT INTO `sys_dictionary_type` VALUES (1, 'Status', '状态', '10', 1, '系统管理员', '2019-11-01 15:57:08', 1, '系统管理员', '2019-11-01 15:58:47');

-- ----------------------------
-- Table structure for sys_district
-- ----------------------------
DROP TABLE IF EXISTS `sys_district`;
CREATE TABLE `sys_district`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `province_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '省-编码',
  `province_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '省-名称',
  `city_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '城市-编码',
  `city_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '城市-名称',
  `district_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '区/县-编码',
  `district_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '区/县-名称',
  `status` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '状态: 10：有效，99：无效',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_province`(`province_code`) USING BTREE COMMENT '省份-索引',
  INDEX `idx_city`(`city_code`) USING BTREE COMMENT '城市-索引',
  INDEX `idx_district`(`district_code`) USING BTREE COMMENT '地区-索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统模块-地区信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_email_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_email_log`;
CREATE TABLE `sys_email_log`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮件类型 数据字典',
  `subject` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮件标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮件内容',
  `send_email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送邮箱',
  `receive_mails` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接收邮箱',
  `status` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'success 发送成功 ，error 发送失败 ',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送状态信息 发送成功、异常信息',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统模块-邮件记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_email_log
-- ----------------------------
INSERT INTO `sys_email_log` VALUES (1, 'VerifyEmail', '邮箱验证', '注册的验证码为qww6s2', 'ikuisme@163.com:', 'healerjean@163.com', '99', '535 Error: authentication failed\n', '2019-11-01 17:23:23', '2019-11-01 17:23:23');
INSERT INTO `sys_email_log` VALUES (2, 'VerifyEmail', '邮箱验证', '注册的验证码为2wn41k', 'ikuisme@163.com:', 'healerjean@163.com', '99', '535 Error: authentication failed\n', '2019-11-01 17:27:00', '2019-11-01 17:27:00');
INSERT INTO `sys_email_log` VALUES (3, 'VerifyEmail', '邮箱验证', '注册的验证码为4mx1k6', 'test.healerjean@163.com:', 'healerjean@163.com', '99', '535 Error: authentication failed\n', '2019-11-01 17:35:29', '2019-11-01 17:35:29');
INSERT INTO `sys_email_log` VALUES (4, 'VerifyEmail', '邮箱验证', '注册的验证码为gdmgws', 'test.healerjean@163.com:', 'healerjean@163.com', '99', '535 Error: authentication failed\n', '2019-11-01 17:37:55', '2019-11-01 17:37:55');
INSERT INTO `sys_email_log` VALUES (5, 'VerifyEmail', '邮箱验证', '注册的验证码为x9y9us', 'test.healerjean@163.com:', 'healerjean@163.com', '99', '535 Error: authentication failed\n', '2019-11-01 17:39:43', '2019-11-01 17:39:43');
INSERT INTO `sys_email_log` VALUES (6, 'VerifyEmail', '邮箱验证', '注册的验证码为gbg67w', 'test.healerjean@163.com:', 'client_register@gmail.com', '99', '535 Error: authentication failed\n', '2019-11-01 17:41:45', '2019-11-01 17:41:45');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ref_system_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '系统code',
  `menu_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '菜单调用地址',
  `method` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '调用方法（get，post，put，delete）',
  `pid` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '父级id',
  `pchain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '父链id，“,”分隔',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述',
  `sort` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '显示排序',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '菜单图标',
  `front_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '前端菜单标识（前端菜单唯一标识）',
  `is_permission` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '是否需要权限拦截，10：需要，99：不需要',
  `menu_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '菜单类型：0: 后端路径, 1:前端菜单，2:非展示前端菜单',
  `status` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `create_user` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新人',
  `update_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统模块-菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 'hlj-manage', '用户管理', '/web/user/manage', 'get', 0, '0', '顶层菜单', 1, '', '', '10', '1', '10', 0, '', '2019-11-01 11:37:28', 0, '', '2019-11-01 13:44:26');
INSERT INTO `sys_menu` VALUES (2, 'hlj-manage', '系统管理', '/web/sys/manage', 'get', 0, '0', '顶层菜单', 2, '', '', '10', '1', '10', 0, '', '2019-11-01 13:36:57', 0, '', '2019-11-01 13:44:47');
INSERT INTO `sys_menu` VALUES (3, 'hlj-manage', '字典管理', '/web/sys/dictionary/manage', 'get', 2, '2,0', '二级菜单', 11, '', '', '10', '1', '10', 0, '', '2019-11-01 13:43:35', 0, '', '2019-11-01 15:26:28');
INSERT INTO `sys_menu` VALUES (4, 'hlj-manage', '字典管理-字典类型添加', '/hlj/sys/dictType/add', 'post', 3, '3,2,0', '', 1, '', '', '10', '0', '10', 0, '', '2019-11-01 13:38:49', 0, '', '2019-11-01 13:47:59');
INSERT INTO `sys_menu` VALUES (5, 'hlj-manage', '字典管理-字典类型修改', '/hlj/sys/dictType/*', 'put', 3, '3,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 13:45:54', 0, '', '2019-11-01 13:55:30');
INSERT INTO `sys_menu` VALUES (6, 'hlj-manage', '字典管理-字典类型删除', '/hlj/sys/dictType/*', 'delete', 3, '3,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 13:46:45', 0, '', '2019-11-01 13:55:31');
INSERT INTO `sys_menu` VALUES (7, 'hlj-manage', '字典管理-字典类型查询', '/hlj/sys/dictType/*', 'get', 3, '3,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 13:49:00', 0, '', '2019-11-01 13:55:33');
INSERT INTO `sys_menu` VALUES (8, 'hlj-manage', '字典管理-字典类型列表查询', '/hlj/sys/dictTypes', 'get', 3, '3,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 13:54:41', 0, '', '2019-11-01 13:55:35');
INSERT INTO `sys_menu` VALUES (9, 'hlj-manage', '字典管理-字典数据添加', '/hlj/sys/dictData/add', 'post', 3, '3,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 13:56:07', 0, '', '2019-11-01 15:08:58');
INSERT INTO `sys_menu` VALUES (10, 'hlj-manage', '字典管理-字典数据删除', '/hlj/sys/dictData/*', 'delete', 3, '3,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 13:56:26', 0, '', '2019-11-01 15:08:59');
INSERT INTO `sys_menu` VALUES (11, 'hlj-manage', '字典管理-字典数据修改', '/hlj/sys/dictData/*', 'put', 3, '3,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 13:56:43', 0, '', '2019-11-01 15:09:00');
INSERT INTO `sys_menu` VALUES (12, 'hlj-manage', '字典管理-字典数据查询', '/hlj/sys/dictData/*', 'get', 3, '3,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 13:56:54', 0, '', '2019-11-01 15:09:01');
INSERT INTO `sys_menu` VALUES (13, 'hlj-manage', '字典管理-字典数据列表查询', '/hlj/sys/dictDatas', 'get', 3, '3,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 13:57:08', 0, '', '2019-11-01 15:09:02');
INSERT INTO `sys_menu` VALUES (14, 'hlj-manage', '字典管理-省份数据查询', '/hlj/sys/provinces', 'get', 3, '3,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 13:58:05', 0, '', '2019-11-01 15:09:03');
INSERT INTO `sys_menu` VALUES (15, 'hlj-manage', '字典管理-城市数据查询', '/hlj/sys/citys', 'get', 3, '3,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 13:58:24', 0, '', '2019-11-01 15:09:04');
INSERT INTO `sys_menu` VALUES (16, 'hlj-manage', '字典管理-地区数据查询', '/hlj/sys/districts', 'get', 3, '3,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 13:58:37', 0, '', '2019-11-01 15:09:05');
INSERT INTO `sys_menu` VALUES (17, 'hlj-manage', '部门管理', '/web/sys/department/manage', 'get', 2, '2,0', '二级菜单', 12, '', '', '10', '1', '10', 0, '', '2019-11-01 15:08:20', 0, '', '2019-11-01 15:26:31');
INSERT INTO `sys_menu` VALUES (18, 'hlj-manage', '部门管理-获取树形结构所有部门', '/hlj/sys/departments/tree', 'get', 17, '17,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:11:07', 0, '', '2019-11-01 15:11:07');
INSERT INTO `sys_menu` VALUES (19, 'hlj-manage', '部门管理-部门添加', '/hlj/sys/departments/add', 'post', 17, '17,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:11:43', 0, '', '2019-11-01 15:11:43');
INSERT INTO `sys_menu` VALUES (20, 'hlj-manage', '部门管理-部门修改', '/hlj/sys/departments/*', 'put', 17, '17,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:12:10', 0, '', '2019-11-01 15:12:10');
INSERT INTO `sys_menu` VALUES (21, 'hlj-manage', '部门管理-部门删除', '/hlj/sys/departments/*', 'delete', 17, '17,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:12:36', 0, '', '2019-11-01 15:12:36');
INSERT INTO `sys_menu` VALUES (22, 'hlj-manage', '菜单管理', '/web/sys/menu/manage', 'get', 2, '2,0', '二级菜单', 13, '', '', '10', '1', '10', 0, '', '2019-11-01 15:16:22', 0, '', '2019-11-01 15:26:34');
INSERT INTO `sys_menu` VALUES (23, 'hlj-manage', '菜单管理-菜单添加', '/hlj/sys/menu/add', 'post', 21, '21,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:14:37', 0, '', '2019-11-01 15:16:54');
INSERT INTO `sys_menu` VALUES (24, 'hlj-manage', '菜单管理-菜单删除', '/hlj/sys/menu', 'delete', 21, '21,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:17:49', 0, '', '2019-11-01 17:13:12');
INSERT INTO `sys_menu` VALUES (25, 'hlj-manage', '菜单管理-菜单修改', '/hlj/sys/menu/*', 'put', 21, '21,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:18:13', 0, '', '2019-11-01 15:18:13');
INSERT INTO `sys_menu` VALUES (26, 'hlj-manage', '菜单管理-菜单查询', '/hlj/sys/menu/*', 'get', 21, '21,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:18:31', 0, '', '2019-11-01 15:18:31');
INSERT INTO `sys_menu` VALUES (27, 'hlj-manage', '菜单管理-菜单列表查询', '/hlj/sys/menus', 'get', 21, '21,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:18:45', 0, '', '2019-11-01 15:18:45');
INSERT INTO `sys_menu` VALUES (28, 'hlj-manage', '角色管理', '/web/sys/role/manage', 'get', 2, '2,0', '二级菜单', 14, '', '', '10', '1', '10', 0, '', '2019-11-01 15:21:28', 0, '', '2019-11-01 15:26:37');
INSERT INTO `sys_menu` VALUES (29, 'hlj-manage', '角色管理-角色添加', '/hlj/sys/role/add', 'post', 28, '28,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:22:42', 0, '', '2019-11-01 15:22:42');
INSERT INTO `sys_menu` VALUES (30, 'hlj-manage', '角色管理-角色删除', '/hlj/sys/role/*', 'delete', 28, '28,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:25:33', 0, '', '2019-11-01 15:25:33');
INSERT INTO `sys_menu` VALUES (31, 'hlj-manage', '角色管理-角色修改', '/hlj/sys/role/*', 'put', 28, '28,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:25:50', 0, '', '2019-11-01 15:25:50');
INSERT INTO `sys_menu` VALUES (32, 'hlj-manage', '角色管理-角色查询', '/hlj/sys/role/*', 'get', 28, '28,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:26:00', 0, '', '2019-11-01 15:26:00');
INSERT INTO `sys_menu` VALUES (33, 'hlj-manage', '角色管理-角色列表查询', '/hlj/sys/roles', 'get', 28, '28,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:26:13', 0, '', '2019-11-01 15:26:13');
INSERT INTO `sys_menu` VALUES (34, 'hlj-manage', '角色管理-角色权限查询', '/hlj/sys/role/*/menus', 'get', 28, '28,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:27:05', 0, '', '2019-11-01 15:27:05');
INSERT INTO `sys_menu` VALUES (35, 'hlj-manage', '角色管理-角色权限修改', '/hlj/sys/role/*/menus', 'put', 28, '28,2,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:27:47', 0, '', '2019-11-01 15:27:47');
INSERT INTO `sys_menu` VALUES (36, 'hlj-manage', '用户管理-用户添加', '/hlj/user/add', 'post', 1, '1,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:30:33', 0, '', '2019-11-01 15:30:33');
INSERT INTO `sys_menu` VALUES (37, 'hlj-manage', '用户管理-用户登出', '/hlj/user/logout', 'get', 1, '1,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:31:36', 0, '', '2019-11-01 15:31:36');
INSERT INTO `sys_menu` VALUES (38, 'hlj-manage', '用户管理-当前用户查询', '/hlj/user/current', 'get', 1, '1,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:31:55', 0, '', '2019-11-01 15:31:55');
INSERT INTO `sys_menu` VALUES (39, 'hlj-manage', '用户管理-当前用户菜单查询', '/hlj/user/current/menus', 'get', 1, '1,0', '', 0, '', '', '10', '0', '10', 0, '', '2019-11-01 15:32:15', 0, '', '2019-11-01 15:32:15');
INSERT INTO `sys_menu` VALUES (40, 'scf-client', '用户管理', '/web/user/manage', 'get', 0, '0', '顶层菜单', 1, '', '', '10', '1', '10', 1, '系统管理员', '2019-11-01 17:09:10', 0, '', '2019-11-01 17:52:15');
INSERT INTO `sys_menu` VALUES (41, 'scf-client', '用户管理-当前用户查询', '/hlj/user/current', 'get', 40, '40,0', '', 0, '', '', '10', '0', '10', 1, '系统管理员', '2019-11-01 17:10:23', 0, '', '2019-11-01 17:10:23');
INSERT INTO `sys_menu` VALUES (42, 'scf-client', '用户管理-test', '/hlj/user/test', 'get', 40, '40,0', '', 0, '', '', '10', '0', '10', 1, '系统管理员', '2019-11-01 17:11:07', 1, '系统管理员', '2019-11-01 17:17:15');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `ref_system_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '系统code',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述',
  `status` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `create_user` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新人',
  `update_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统模块-角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', 'hlj-manage', '系统管理员', '10', 0, '', '2019-10-31 22:28:07', 0, '', '2019-10-31 22:28:07');
INSERT INTO `sys_role` VALUES (2, '客户端', 'hlj-client', '客户端用户角色', '10', 1, '系统管理员', '2019-11-01 16:13:49', 1, '系统管理员', '2019-11-01 16:26:44');
INSERT INTO `sys_role` VALUES (3, '运营', 'hlj-manage', '运营角色', '10', 1, '系统管理员', '2019-11-01 16:27:31', 0, '', '2019-11-01 16:29:06');
INSERT INTO `sys_role` VALUES (4, '审核专员', 'hlj-manage', '审核专员', '10', 1, '系统管理员', '2019-11-01 16:27:56', 0, '', '2019-11-01 16:31:25');
INSERT INTO `sys_role` VALUES (5, '测试', 'hlj-manage', '测试专员', '10', 1, '系统管理员', '2019-11-01 16:27:46', 0, '', '2019-11-01 16:31:18');

-- ----------------------------
-- Table structure for sys_role_menu_ref
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu_ref`;
CREATE TABLE `sys_role_menu_ref`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ref_role_id` bigint(20) UNSIGNED NOT NULL COMMENT '角色id',
  `ref_menu_id` bigint(20) UNSIGNED NOT NULL COMMENT '菜单id',
  `status` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `create_user` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新人',
  `update_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_id`(`ref_role_id`) USING BTREE COMMENT '角色id索引'
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统模块-角色与菜单关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu_ref
-- ----------------------------
INSERT INTO `sys_role_menu_ref` VALUES (1, 1, 1, '10', 0, '', '2019-10-31 22:30:30', 0, '', '2019-10-31 22:30:30');
INSERT INTO `sys_role_menu_ref` VALUES (2, 1, 2, '10', 0, '', '2019-11-01 15:38:15', 0, '', '2019-11-01 15:38:15');
INSERT INTO `sys_role_menu_ref` VALUES (3, 1, 3, '10', 0, '', '2019-11-01 15:38:15', 0, '', '2019-11-01 15:38:15');
INSERT INTO `sys_role_menu_ref` VALUES (4, 1, 4, '10', 0, '', '2019-11-01 15:38:15', 0, '', '2019-11-01 15:38:15');
INSERT INTO `sys_role_menu_ref` VALUES (5, 1, 5, '10', 0, '', '2019-11-01 15:38:15', 0, '', '2019-11-01 15:38:15');
INSERT INTO `sys_role_menu_ref` VALUES (6, 1, 6, '10', 0, '', '2019-11-01 15:38:15', 0, '', '2019-11-01 15:38:15');
INSERT INTO `sys_role_menu_ref` VALUES (7, 1, 7, '10', 0, '', '2019-11-01 15:38:15', 0, '', '2019-11-01 15:38:15');
INSERT INTO `sys_role_menu_ref` VALUES (8, 1, 8, '10', 0, '', '2019-11-01 15:38:15', 0, '', '2019-11-01 15:38:15');
INSERT INTO `sys_role_menu_ref` VALUES (9, 1, 9, '10', 0, '', '2019-11-01 15:38:15', 0, '', '2019-11-01 15:38:15');
INSERT INTO `sys_role_menu_ref` VALUES (10, 1, 10, '10', 0, '', '2019-11-01 15:38:15', 0, '', '2019-11-01 15:38:15');
INSERT INTO `sys_role_menu_ref` VALUES (11, 1, 11, '10', 0, '', '2019-11-01 15:38:15', 0, '', '2019-11-01 15:38:15');
INSERT INTO `sys_role_menu_ref` VALUES (12, 1, 12, '10', 0, '', '2019-11-01 15:38:15', 0, '', '2019-11-01 15:38:15');
INSERT INTO `sys_role_menu_ref` VALUES (13, 1, 13, '10', 0, '', '2019-11-01 15:38:15', 0, '', '2019-11-01 15:38:15');
INSERT INTO `sys_role_menu_ref` VALUES (14, 1, 14, '10', 0, '', '2019-11-01 15:38:15', 0, '', '2019-11-01 15:38:15');
INSERT INTO `sys_role_menu_ref` VALUES (15, 1, 15, '10', 0, '', '2019-11-01 15:38:15', 0, '', '2019-11-01 15:38:15');
INSERT INTO `sys_role_menu_ref` VALUES (16, 1, 16, '10', 0, '', '2019-11-01 15:38:15', 0, '', '2019-11-01 15:38:15');
INSERT INTO `sys_role_menu_ref` VALUES (17, 1, 17, '10', 0, '', '2019-11-01 15:38:15', 0, '', '2019-11-01 15:38:15');
INSERT INTO `sys_role_menu_ref` VALUES (18, 1, 18, '10', 0, '', '2019-11-01 15:38:15', 0, '', '2019-11-01 15:38:15');
INSERT INTO `sys_role_menu_ref` VALUES (19, 1, 19, '10', 0, '', '2019-11-01 15:38:15', 0, '', '2019-11-01 15:38:15');
INSERT INTO `sys_role_menu_ref` VALUES (20, 1, 20, '10', 0, '', '2019-11-01 15:38:16', 0, '', '2019-11-01 15:38:16');
INSERT INTO `sys_role_menu_ref` VALUES (21, 1, 21, '10', 0, '', '2019-11-01 15:38:16', 0, '', '2019-11-01 15:38:16');
INSERT INTO `sys_role_menu_ref` VALUES (22, 1, 22, '10', 0, '', '2019-11-01 15:38:16', 0, '', '2019-11-01 15:38:16');
INSERT INTO `sys_role_menu_ref` VALUES (23, 1, 23, '10', 0, '', '2019-11-01 15:38:16', 0, '', '2019-11-01 15:38:16');
INSERT INTO `sys_role_menu_ref` VALUES (24, 1, 24, '10', 0, '', '2019-11-01 15:38:16', 0, '', '2019-11-01 15:39:08');
INSERT INTO `sys_role_menu_ref` VALUES (25, 1, 25, '10', 0, '', '2019-11-01 15:38:16', 0, '', '2019-11-01 15:39:16');
INSERT INTO `sys_role_menu_ref` VALUES (26, 1, 26, '10', 0, '', '2019-11-01 15:38:16', 0, '', '2019-11-01 15:39:18');
INSERT INTO `sys_role_menu_ref` VALUES (27, 1, 27, '10', 0, '', '2019-11-01 15:38:16', 0, '', '2019-11-01 15:39:20');
INSERT INTO `sys_role_menu_ref` VALUES (28, 1, 28, '10', 0, '', '2019-11-01 15:38:16', 0, '', '2019-11-01 15:39:21');
INSERT INTO `sys_role_menu_ref` VALUES (29, 1, 29, '10', 0, '', '2019-11-01 15:38:16', 0, '', '2019-11-01 15:39:24');
INSERT INTO `sys_role_menu_ref` VALUES (30, 1, 30, '10', 0, '', '2019-11-01 15:38:16', 0, '', '2019-11-01 16:37:29');
INSERT INTO `sys_role_menu_ref` VALUES (31, 1, 31, '10', 0, '', '2019-11-01 15:38:16', 0, '', '2019-11-01 15:39:28');
INSERT INTO `sys_role_menu_ref` VALUES (32, 1, 32, '10', 0, '', '2019-11-01 15:38:16', 0, '', '2019-11-01 15:39:30');
INSERT INTO `sys_role_menu_ref` VALUES (33, 1, 33, '10', 0, '', '2019-11-01 15:38:16', 0, '', '2019-11-01 16:39:38');
INSERT INTO `sys_role_menu_ref` VALUES (34, 1, 34, '10', 0, '', '2019-11-01 15:38:16', 0, '', '2019-11-01 15:39:33');
INSERT INTO `sys_role_menu_ref` VALUES (35, 1, 35, '10', 0, '', '2019-11-01 15:38:16', 0, '', '2019-11-01 15:39:35');
INSERT INTO `sys_role_menu_ref` VALUES (36, 1, 36, '10', 0, '', '2019-11-01 15:38:16', 0, '', '2019-11-01 15:39:37');
INSERT INTO `sys_role_menu_ref` VALUES (37, 1, 37, '10', 0, '', '2019-11-01 15:38:16', 0, '', '2019-11-01 15:39:38');
INSERT INTO `sys_role_menu_ref` VALUES (38, 1, 38, '10', 0, '', '2019-11-01 15:38:16', 0, '', '2019-11-01 15:39:40');
INSERT INTO `sys_role_menu_ref` VALUES (39, 1, 39, '10', 0, '', '2019-11-01 15:38:17', 0, '', '2019-11-01 15:39:42');
INSERT INTO `sys_role_menu_ref` VALUES (40, 2, 40, '10', 0, '', '2019-11-01 17:46:43', 0, '', '2019-11-01 17:46:43');
INSERT INTO `sys_role_menu_ref` VALUES (41, 2, 40, '10', 0, '', '2019-11-01 17:46:51', 0, '', '2019-11-01 17:46:51');

-- ----------------------------
-- Table structure for sys_template
-- ----------------------------
DROP TABLE IF EXISTS `sys_template`;
CREATE TABLE `sys_template`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板名称',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板内容',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务类型（邮箱，合同，短信等） 字典表',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '模板描述',
  `status` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `create_user` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新人',
  `update_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统模块-模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_template
-- ----------------------------
INSERT INTO `sys_template` VALUES (1, 'VerifyEmail', '注册的验证码为${VerifyCode}', 'Email', '注册邮箱', '10', 0, '', '2019-11-01 17:23:20', 0, '', '2019-11-01 17:23:20');

-- ----------------------------
-- Table structure for sys_user_department_ref
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_department_ref`;
CREATE TABLE `sys_user_department_ref`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ref_user_id` bigint(20) NOT NULL COMMENT '用户id',
  `ref_department_id` bigint(20) NOT NULL COMMENT '部门Id',
  `status` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '状态: 10：有效，99：无效',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统-用户与部门关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_department_ref
-- ----------------------------
INSERT INTO `sys_user_department_ref` VALUES (1, 1, 1, '10', '2019-11-01 15:46:44');
INSERT INTO `sys_user_department_ref` VALUES (2, 3, 3, '10', '2019-11-01 17:50:58');
INSERT INTO `sys_user_department_ref` VALUES (3, 4, 4, '10', '2019-11-01 17:51:00');
INSERT INTO `sys_user_department_ref` VALUES (4, 5, 5, '10', '2019-11-01 17:50:28');

-- ----------------------------
-- Table structure for sys_user_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_info`;
CREATE TABLE `sys_user_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `real_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实姓名',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱',
  `telephone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `user_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户类型',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户状态',
  `salt` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码随机盐',
  `create_user` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新人',
  `update_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`user_name`) USING BTREE COMMENT '用户名唯一',
  UNIQUE INDEX `uk_email`(`email`) USING BTREE COMMENT '邮箱唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统模块-平台用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_info
-- ----------------------------
INSERT INTO `sys_user_info` VALUES (1, 'admin', '系统管理员', 'admin@gmail.com', '18877779999', '57252102872ea81af069f118c15f8a4e', 'manager', '10', '8aa8ff7f325e1ac742e108a38948dd9c', 0, '', '2019-10-31 22:08:46', 0, '', '2019-11-01 15:57:01');
INSERT INTO `sys_user_info` VALUES (2, 'client', '客户晋', 'client@gmail.com', '16655557777', '57252102872ea81af069f118c15f8a4e', 'webuser', '10', '8aa8ff7f325e1ac742e108a38948dd9c', 0, '', '2019-10-31 22:37:06', 0, '', '2019-11-01 16:31:43');
INSERT INTO `sys_user_info` VALUES (3, 'operete', '运营晋', 'operate@gmail.com', '15566669999', '57252102872ea81af069f118c15f8a4e', 'manager', '10', '8aa8ff7f325e1ac742e108a38948dd9c', 0, '', '2019-11-01 11:44:04', 0, '', '2019-11-01 16:31:40');
INSERT INTO `sys_user_info` VALUES (4, 'audit', '审核晋', 'audit@gmail.com', '16655558888', '57252102872ea81af069f118c15f8a4e', 'manager', '10', '8aa8ff7f325e1ac742e108a38948dd9c', 0, '', '2019-11-01 11:46:30', 0, '', '2019-11-01 15:56:56');
INSERT INTO `sys_user_info` VALUES (5, 'test', '测试晋', 'test@admin.com', '15544443333', '6d3b3b337307d408c0a057dbf8f4dd36', 'manager', '10', '17f32a106265dba9a7b0e07e1d38a297', 1, '系统管理员', '2019-11-01 16:31:01', 1, '系统管理员', '2019-11-01 16:31:01');

-- ----------------------------
-- Table structure for sys_user_role_ref
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role_ref`;
CREATE TABLE `sys_user_role_ref`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ref_user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户id',
  `ref_role_id` bigint(20) UNSIGNED NOT NULL COMMENT '角色id',
  `status` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `create_user` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '更新人',
  `update_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人名称',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`ref_user_id`) USING BTREE COMMENT '用户id索引'
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统模块-用户与角色关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role_ref
-- ----------------------------
INSERT INTO `sys_user_role_ref` VALUES (1, 1, 1, '10', 0, '', '2019-10-31 22:28:23', 0, '', '2019-10-31 22:28:23');
INSERT INTO `sys_user_role_ref` VALUES (2, 2, 2, '10', 0, '', '2019-11-01 11:45:35', 0, '', '2019-11-01 16:31:59');
INSERT INTO `sys_user_role_ref` VALUES (3, 3, 3, '10', 0, '', '2019-11-01 11:45:49', 0, '', '2019-11-01 16:32:01');
INSERT INTO `sys_user_role_ref` VALUES (4, 4, 4, '10', 0, '', '2019-11-01 13:35:09', 0, '', '2019-11-01 16:32:04');
INSERT INTO `sys_user_role_ref` VALUES (5, 5, 5, '10', 0, '', '2019-11-01 16:32:11', 0, '', '2019-11-01 16:32:11');

SET FOREIGN_KEY_CHECKS = 1;

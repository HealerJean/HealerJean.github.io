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

 Date: 14/06/2019 18:59:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for demo_entity
-- ----------------------------
DROP TABLE IF EXISTS `demo_entity`;
CREATE TABLE `demo_entity`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `age` bigint(20) NULL DEFAULT 0,
  `cdate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `udate` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of demo_entity
-- ----------------------------
INSERT INTO `demo_entity` VALUES (1, '张宇晋', 1, '2019-06-13 20:30:51', '2019-06-13 20:30:51');

-- ----------------------------
-- Table structure for hlj_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `hlj_sys_menu`;
CREATE TABLE `hlj_sys_menu`  (
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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统模块-菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hlj_sys_menu
-- ----------------------------
INSERT INTO `hlj_sys_menu` VALUES (1, 'hlj-manager', '系统管理', '1', 'get', '10', 0, '0', NULL, NULL, NULL, NULL, '10', '1', NULL, NULL, '2019-06-04 19:28:14', NULL, NULL, '2019-06-13 15:48:20');
INSERT INTO `hlj_sys_menu` VALUES (2, 'hlj-manager', '列表查询', '/api/users', 'get', '10', 5, '5,0', NULL, NULL, NULL, NULL, '10', '0', NULL, NULL, '2019-06-12 17:16:04', NULL, NULL, '2019-06-13 15:52:41');
INSERT INTO `hlj_sys_menu` VALUES (3, 'hlj-manager', '修改', '/api/user/*', 'put', '10', 5, '5,0', NULL, NULL, NULL, NULL, '10', '0', NULL, NULL, '2019-06-12 17:19:08', NULL, NULL, '2019-06-13 15:52:16');
INSERT INTO `hlj_sys_menu` VALUES (4, 'hlj-manager', '删除', '/api/user/*', 'delete', '10', 5, '5,0', NULL, NULL, NULL, NULL, '10', '0', NULL, NULL, '2019-06-12 17:19:21', NULL, NULL, '2019-06-13 15:52:23');
INSERT INTO `hlj_sys_menu` VALUES (5, 'hlj-manager', '查询', '/api/user/*', 'get', '10', 5, '5,0', NULL, NULL, NULL, NULL, '10', '0', NULL, NULL, '2019-06-12 17:19:35', NULL, NULL, '2019-06-13 15:52:27');
INSERT INTO `hlj_sys_menu` VALUES (6, 'hlj-manager', '新增', '/api/user/add', 'post', '10', 5, '5,0', NULL, NULL, NULL, NULL, '10', '0', NULL, NULL, '2019-06-12 17:21:05', NULL, NULL, '2019-06-13 15:52:34');

-- ----------------------------
-- Table structure for hlj_sys_ref_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `hlj_sys_ref_role_menu`;
CREATE TABLE `hlj_sys_ref_role_menu`  (
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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统模块-角色与菜单关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hlj_sys_ref_role_menu
-- ----------------------------
INSERT INTO `hlj_sys_ref_role_menu` VALUES (1, 1, 1, '10', NULL, NULL, '2019-06-13 15:53:50', NULL, NULL, NULL);
INSERT INTO `hlj_sys_ref_role_menu` VALUES (2, 1, 2, '10', NULL, NULL, '2019-06-13 15:53:58', NULL, NULL, NULL);
INSERT INTO `hlj_sys_ref_role_menu` VALUES (3, 1, 3, '10', NULL, NULL, '2019-06-13 15:54:10', NULL, NULL, NULL);
INSERT INTO `hlj_sys_ref_role_menu` VALUES (4, 1, 4, '10', NULL, NULL, '2019-06-13 15:54:21', NULL, NULL, NULL);
INSERT INTO `hlj_sys_ref_role_menu` VALUES (5, 1, 5, '10', NULL, NULL, '2019-06-13 15:54:29', NULL, NULL, NULL);
INSERT INTO `hlj_sys_ref_role_menu` VALUES (6, 1, 6, '10', NULL, NULL, '2019-06-13 15:54:35', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for hlj_sys_ref_user_role
-- ----------------------------
DROP TABLE IF EXISTS `hlj_sys_ref_user_role`;
CREATE TABLE `hlj_sys_ref_user_role`  (
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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统模块-用户与角色关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hlj_sys_ref_user_role
-- ----------------------------
INSERT INTO `hlj_sys_ref_user_role` VALUES (1, 1, 1, '10', 1, 'HealerJean', '2019-06-13 15:43:02', 1, 'HealerJean', NULL);

-- ----------------------------
-- Table structure for hlj_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `hlj_sys_role`;
CREATE TABLE `hlj_sys_role`  (
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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统模块-角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hlj_sys_role
-- ----------------------------
INSERT INTO `hlj_sys_role` VALUES (1, '运营', 'manager', '10', '运营人员', 1, 'HealerJean', '2019-06-13 15:43:42', 1, 'HealerJean', '2019-06-13 15:43:48');

-- ----------------------------
-- Table structure for hlj_user_info
-- ----------------------------
DROP TABLE IF EXISTS `hlj_user_info`;
CREATE TABLE `hlj_user_info`  (
  `id` bigint(19) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hlj_user_info
-- ----------------------------
INSERT INTO `hlj_user_info` VALUES (1, 'HealerJean', 'password');

SET FOREIGN_KEY_CHECKS = 1;

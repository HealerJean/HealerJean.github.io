/*
 Navicat Premium Data Transfer

 Source Server         : duodian
 Source Server Type    : MySQL
 Source Server Version : 50634
 Source Host           : rds7b1n6yxdvko77nhv6o.mysql.rds.aliyuncs.com
 Source Database       : admore

 Target Server Type    : MySQL
 Target Server Version : 50634
 File Encoding         : utf-8

 Date: 03/02/2018 19:20:29 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `sys_auth_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_role_menu`;
CREATE TABLE `sys_auth_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menuId` bigint(20) DEFAULT NULL,
  `roleId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9277 DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;

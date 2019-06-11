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

 Date: 03/02/2018 19:19:46 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `sys_admin_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_admin_user`;
CREATE TABLE `sys_admin_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `email` varchar(60) DEFAULT NULL,
  `headImage` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `cuserid` bigint(20) DEFAULT NULL,
  `isSuper` int(2) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;

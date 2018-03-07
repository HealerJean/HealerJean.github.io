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

 Date: 03/02/2018 19:11:08 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `workflow_task_unread_badge`
-- ----------------------------
DROP TABLE IF EXISTS `workflow_task_unread_badge`;
CREATE TABLE `workflow_task_unread_badge` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `admId` bigint(20) DEFAULT NULL,
  `pageType` int(11) DEFAULT NULL,
  `taskId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_admId` (`admId`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;

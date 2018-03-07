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

 Date: 03/02/2018 19:10:57 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `workflow_task_default_appr_cc`
-- ----------------------------
DROP TABLE IF EXISTS `workflow_task_default_appr_cc`;
CREATE TABLE `workflow_task_default_appr_cc` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `admId` bigint(20) DEFAULT NULL,
  `admType` int(11) DEFAULT NULL,
  `examineOrder` int(11) DEFAULT NULL,
  `taskType` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `type_adm_uniq` (`taskType`,`admId`)
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;

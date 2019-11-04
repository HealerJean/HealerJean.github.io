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

 Date: 03/02/2018 19:11:03 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `workflow_task_examine_details`
-- ----------------------------
DROP TABLE IF EXISTS `workflow_task_examine_details`;
CREATE TABLE `workflow_task_examine_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `admId` bigint(20) DEFAULT NULL,
  `admType` int(11) DEFAULT NULL,
  `cdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `examineOrder` int(11) DEFAULT NULL,
  `examineStatus` int(11) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `taskId` bigint(20) DEFAULT NULL,
  `udate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_adm_uniq` (`taskId`,`admId`,`examineStatus`)
) ENGINE=InnoDB AUTO_INCREMENT=837 DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;

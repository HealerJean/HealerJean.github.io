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

 Date: 03/02/2018 19:10:45 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `workflow_task`
-- ----------------------------
DROP TABLE IF EXISTS `workflow_task`;
CREATE TABLE `workflow_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `cuserId` bigint(20) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `taskType` int(11) DEFAULT NULL,
  `udate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `cuserType` int(11) DEFAULT NULL,
  `trackId` varchar(50) DEFAULT NULL,
  `allowType` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=269 DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;

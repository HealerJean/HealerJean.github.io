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

 Date: 03/02/2018 19:10:20 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `user_old_background_balance_trannsfer`
-- ----------------------------
DROP TABLE IF EXISTS `user_old_background_balance_trannsfer`;
CREATE TABLE `user_old_background_balance_trannsfer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `adminId` bigint(20) DEFAULT NULL,
  `customerId` bigint(20) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  `balance` decimal(19,2) DEFAULT NULL,
  `customerName` varchar(100) DEFAULT NULL,
  `userName` varchar(200) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `auditStatus` int(11) DEFAULT NULL,
  `cdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;

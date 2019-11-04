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

 Date: 03/02/2018 19:10:28 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `user_old_background_balance_trannsfer_attachment`
-- ----------------------------
DROP TABLE IF EXISTS `user_old_background_balance_trannsfer_attachment`;
CREATE TABLE `user_old_background_balance_trannsfer_attachment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `eventId` bigint(20) DEFAULT NULL,
  `fileName` varchar(100) DEFAULT NULL,
  `fileUrl` varchar(100) DEFAULT NULL,
  `cdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fileType` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;

/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : localhost
 Source Database       : casnew

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : utf-8

 Date: 03/13/2018 22:06:19 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `SYS_USER`
-- ----------------------------
DROP TABLE IF EXISTS `SYS_USER`;
CREATE TABLE `SYS_USER` (
  `USERNAME` varchar(30) COLLATE utf8_bin NOT NULL,
  `PASSWORD` varchar(64) COLLATE utf8_bin NOT NULL,
  `EMAIL` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `ADDRESS` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `AGE` int(11) DEFAULT NULL,
  `EXPIRED` int(11) DEFAULT NULL,
  `DISABLE` int(11) DEFAULT NULL,
  PRIMARY KEY (`USERNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `SYS_USER`
-- ----------------------------
BEGIN;
INSERT INTO `SYS_USER` VALUES ('HealerJean', '40e5fd27a2f9db33d397d11617c2098b', 'mxzdhealer@gmail.com', '山西忻州', '24', '0', '0'), ('admin', '202cb962ac59075b964b07152d234b70', 'huang.wenbin@foxmail.com', '广州天河', '24', '0', '0'), ('wangwu', '827ccb0eea8a706c4c34a16891f84e7b', 'wangwu@foxmail.com', '广州番禺', '27', '1', '0'), ('zhangsan', '25d55ad283aa400af464c76d713c07ad', 'zhangsan@foxmail.com', '广州越秀', '26', '0', '0'), ('zhaosi', '81dc9bdb52d04dc20036dbd8313ed055', 'zhaosi@foxmail.com', '广州海珠', '25', '0', '1');
COMMIT;

-- ----------------------------
--  Table structure for `SYS_USER_ENCODE`
-- ----------------------------
DROP TABLE IF EXISTS `SYS_USER_ENCODE`;
CREATE TABLE `SYS_USER_ENCODE` (
  `USERNAME` varchar(30) COLLATE utf8_bin NOT NULL,
  `PASSWORD` varchar(64) COLLATE utf8_bin NOT NULL,
  `EMAIL` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `ADDRESS` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `AGE` int(11) DEFAULT NULL,
  `EXPIRED` int(11) DEFAULT NULL,
  `DISABLED` int(11) DEFAULT NULL,
  PRIMARY KEY (`USERNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `SYS_USER_ENCODE`
-- ----------------------------
BEGIN;
INSERT INTO `SYS_USER_ENCODE` VALUES ('admin_en', 'bfb194d5bd84a5fc77c1d303aefd36c3', 'huang.wenbin@foxmail.com', '江门蓬江', '24', '0', '0'), ('wangwu_en', '44b907d6fee23a552348eabf5fcf1ac7', 'wangwu@foxmail.com', '佛山顺德', '19', '1', '0'), ('zhangsan_en', '68ae075edf004353a0403ee681e45056', 'zhangsan@foxmail.com', '深圳宝安', '21', '0', '0'), ('zhaosi_en', 'd66108d0409f68af538301b637f13a18', 'zhaosi@foxmail.com', '清远清新', '20', '0', '1');
COMMIT;

-- ----------------------------
--  Table structure for `SYS_USER_QUESTION`
-- ----------------------------
DROP TABLE IF EXISTS `SYS_USER_QUESTION`;
CREATE TABLE `SYS_USER_QUESTION` (
  `USERNAME` varchar(30) COLLATE utf8_bin NOT NULL,
  `QUESTION` varchar(200) COLLATE utf8_bin NOT NULL,
  `ANSWER` varchar(100) COLLATE utf8_bin NOT NULL,
  `email` varchar(100) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `SYS_USER_QUESTION`
-- ----------------------------
BEGIN;
INSERT INTO `SYS_USER_QUESTION` VALUES ('admin', '你的年龄是？', '24', 'huang.wenbin@foxmail.com'), ('HealerJean', '我的名字是？', 'HealerJean', 'mxzdhealer@gmail.com'), ('zhangsan', '我在哪里工作？', 'guangzhou', 'zhangsan@foxmail.com'), ('HealerJean', '你喜欢的人是？', 'Liuli', 'mxzdhealer@gmail.com');
COMMIT;

-- ----------------------------
--  Table structure for `sys_user_rest_salt`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_rest_salt`;
CREATE TABLE `sys_user_rest_salt` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `age` int(11) NOT NULL,
  `disable` int(11) NOT NULL,
  `email` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `expired` int(11) NOT NULL,
  `password` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `salt` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `username` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `locked` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `sys_user_rest_salt`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_rest_salt` VALUES ('1', '山西忻州', '24', '0', 'mxzdhealer@gmail.com', '0', '971c4fef68430b0de3b91203cd27fd6a', 'aa6abe8f4c94beae41f94c62326500ca', 'HealerJean', '0'), ('2', '北京西城', '24', '0', 'huang.wenbin@gmail.com', '0', '971c4fef68430b0de3b91203cd27fd6a', 'aa6abe8f4c94beae41f94c62326500ca', 'admin2 ', '1'), ('3', '江苏南京', '24', '1', 'zhangsan@gmail.com', '0', '971c4fef68430b0de3b91203cd27fd6a', 'aa6abe8f4c94beae41f94c62326500ca', 'zhangsan', '0'), ('4', '浙江杭州', '24', '0', 'zhaosi@gmail.com', '1', '971c4fef68430b0de3b91203cd27fd6a', 'aa6abe8f4c94beae41f94c62326500ca', 'zhaosi', '0');
COMMIT;

-- ----------------------------
--  Table structure for `sys_user_rest_salt_attrs`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_rest_salt_attrs`;
CREATE TABLE `sys_user_rest_salt_attrs` (
  `EMAIL` varchar(100) COLLATE utf8_bin NOT NULL,
  `ATTR_KEY` varchar(50) COLLATE utf8_bin NOT NULL,
  `ATTR_VAL` varchar(100) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
--  Records of `sys_user_rest_salt_attrs`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_rest_salt_attrs` VALUES ('mxzdhealer@gmail.com', 'group', '软件研究所'), ('mxzdhealer@gmail.com', 'group', '计算机132'), ('mxzdhealer@gmail.com', 'school', '大连工业大学'), ('mxzdhealer@gmail.com', 'school', '北京大学'), ('mxzdhealer@gmail.com', 'school', '清华大学'), ('zhangsan@gmail.com', 'group', 'DEV_ROLE');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

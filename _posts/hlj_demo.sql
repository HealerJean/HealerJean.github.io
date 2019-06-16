/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : localhost:3306
 Source Schema         : hlj_demo

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : 65001

 Date: 17/06/2019 00:13:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for demo_entity
-- ----------------------------
DROP TABLE IF EXISTS `demo_entity`;
CREATE TABLE `demo_entity` (
  `id` bigint(16) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `phone` varchar(20) DEFAULT '' COMMENT '手机号',
  `email` varchar(64) DEFAULT '' COMMENT '邮箱',
  `age` int(100) DEFAULT NULL,
  `del_flag` varchar(8) NOT NULL COMMENT '10可用，99删除',
  `create_user` bigint(16) unsigned DEFAULT NULL COMMENT '创建人',
  `create_name` varchar(64) DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_user` bigint(16) unsigned DEFAULT NULL COMMENT '更新人',
  `update_name` varchar(64) DEFAULT '' COMMENT '更新人名称',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `new_column` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of demo_entity
-- ----------------------------
BEGIN;
INSERT INTO `demo_entity` VALUES (1, 'HealerJean', '18842637651', 'healerjean@gmail.com', 25, '10', NULL, NULL, '2019-06-16 02:01:48', NULL, NULL, '2019-06-16 02:01:48', NULL);
INSERT INTO `demo_entity` VALUES (2, 'Heler', '17610397651', 'healerjean@163.com', 23, '10', NULL, NULL, '2019-06-16 04:03:05', NULL, NULL, '2019-06-16 04:03:05', NULL);
INSERT INTO `demo_entity` VALUES (3, 'zhangyujin', '17610397651', 'healerjean@gmail.com', 55, '10', NULL, NULL, '2019-06-16 12:11:33', NULL, NULL, '2019-06-16 12:11:33', NULL);
INSERT INTO `demo_entity` VALUES (4, 'zhangyujin', '17610397651', 'healerjean@gmail.com', 55, '10', NULL, NULL, '2019-06-16 12:12:51', NULL, NULL, '2019-06-16 12:12:51', NULL);
INSERT INTO `demo_entity` VALUES (5, 'zhangyujin', '17610397651', 'healerjean@gmail.com', 55, '10', NULL, NULL, '2019-06-16 12:24:28', NULL, NULL, '2019-06-16 12:24:28', NULL);
INSERT INTO `demo_entity` VALUES (6, 'zhangyujin', '17610397651', 'healerjean@gmail.com', 55, '10', NULL, NULL, '2019-06-16 12:24:32', NULL, NULL, '2019-06-16 12:24:32', NULL);
INSERT INTO `demo_entity` VALUES (7, 'zhangyujin', '17610397651', 'healerjean@gmail.com', 55, '10', NULL, NULL, '2019-06-16 12:24:48', NULL, NULL, '2019-06-16 12:24:48', NULL);
INSERT INTO `demo_entity` VALUES (8, 'zhangyujin', '17610397651', 'healerjean@gmail.com', 55, '10', NULL, NULL, '2019-06-16 12:51:51', NULL, NULL, '2019-06-16 12:51:51', NULL);
INSERT INTO `demo_entity` VALUES (9, 'zhangyujin', '17610397651', 'healerjean@gmail.com', 55, '10', NULL, NULL, '2019-06-16 12:51:54', NULL, NULL, '2019-06-16 12:51:54', NULL);
INSERT INTO `demo_entity` VALUES (10, 'zhangyujin', '17610397651', 'healerjean@gmail.com', 55, '10', NULL, NULL, '2019-06-16 12:51:55', NULL, NULL, '2019-06-16 12:51:55', NULL);
INSERT INTO `demo_entity` VALUES (11, 'zhangyujin', '17610397651', 'healerjean@gmail.com', 55, '10', NULL, NULL, '2019-06-16 12:51:56', NULL, NULL, '2019-06-16 12:51:56', NULL);
INSERT INTO `demo_entity` VALUES (12, 'zhangyujin', '17610397651', 'healerjean@gmail.com', 55, '10', NULL, NULL, '2019-06-16 13:03:17', NULL, NULL, '2019-06-16 13:03:17', NULL);
INSERT INTO `demo_entity` VALUES (13, 'zhangyujin', '17610397651', 'healerjean@gmail.com', 55, '10', NULL, NULL, '2019-06-16 13:03:19', NULL, NULL, '2019-06-16 13:03:19', NULL);
INSERT INTO `demo_entity` VALUES (14, 'zhangyujin', '17610397651', 'healerjean@gmail.com', 55, '10', NULL, NULL, '2019-06-16 13:07:14', NULL, NULL, '2019-06-16 13:07:14', NULL);
INSERT INTO `demo_entity` VALUES (15, 'zhangyujin', '17610397651', 'healerjean@gmail.com', 55, '10', NULL, NULL, '2019-06-16 13:07:16', NULL, NULL, '2019-06-16 13:07:16', NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

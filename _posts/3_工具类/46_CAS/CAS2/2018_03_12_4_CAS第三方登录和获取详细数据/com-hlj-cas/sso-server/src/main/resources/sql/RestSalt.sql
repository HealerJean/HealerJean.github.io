use casnew;

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
);


  INSERT INTO `sys_user_rest_salt` VALUES
('1', '山西忻州', '24', '0', 'mxzdhealer@gmail.com', '0', '971c4fef68430b0de3b91203cd27fd6a', 'aa6abe8f4c94beae41f94c62326500ca', 'HealerJean', '0'),
('2', '北京西城', '24', '0', 'huang.wenbin@gmail.com', '0', '971c4fef68430b0de3b91203cd27fd6a', 'aa6abe8f4c94beae41f94c62326500ca', 'admin2 ', '0'),
  /*锁定用户*/
('3', '江苏南京', '24', '1', 'zhangsan@gmail.com', '0', '971c4fef68430b0de3b91203cd27fd6a', 'aa6abe8f4c94beae41f94c62326500ca', 'zhangsan', '0'),
  /*不可用*/
('4', '浙江杭州', '24', '0', 'zhaosi@gmail.com', '1', '971c4fef68430b0de3b91203cd27fd6a', 'aa6abe8f4c94beae41f94c62326500ca', 'zhaosi', '0');



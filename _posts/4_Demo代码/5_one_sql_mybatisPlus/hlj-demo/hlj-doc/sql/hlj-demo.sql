
    CREATE TABLE `demo_entity` (
        `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
        `name` varchar(64) NOT NULL,
        `phone` varchar(20) DEFAULT '' COMMENT '手机号',
        `email` varchar(64) DEFAULT '' COMMENT '邮箱',
        `age` int(10) DEFAULT NULL,
        `status` varchar(8) NOT NULL COMMENT '状态',
        `create_user` bigint(16) unsigned DEFAULT NULL COMMENT '创建人',
        `create_name` varchar(64) DEFAULT '' COMMENT '创建人名称',
        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_user` bigint(16) unsigned DEFAULT NULL COMMENT '更新人',
        `update_name` varchar(64) DEFAULT '' COMMENT '更新人名称',
        `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

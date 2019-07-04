CREATE TABLE `base_msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `msg` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_id` varchar(100) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_id` varchar(100) DEFAULT NULL,
  `state` smallint(1) DEFAULT '0' COMMENT '状态：0-初始，1-正常，2-驳回',
  PRIMARY KEY (`id`),
  UNIQUE KEY `INDEX_MSG` (`msg`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;



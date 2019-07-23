-- 业务请求流水记录表
CREATE TABLE `busi_request` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `busi_type` smallint(6) NOT NULL COMMENT '业务类型：10-流程部署，20-流程开启',
  `source` tinyint(4) NOT NULL COMMENT '请求来源',
  `client_req_no` varchar(30) NOT NULL COMMENT '请求流水号',
  `status` tinyint(4) NOT NULL COMMENT '请求状态：0-初始化，1-处理成功，2-处理失败',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IDX_CLIENTREQNO_SOURCE` (`client_req_no`,`busi_type`) USING BTREE COMMENT '请求流水号索引，业务唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务请求流水表';

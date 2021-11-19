DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '账户Id',
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '邮箱',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '账户名',
  `password` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '密码',
  `register_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `login_time` datetime DEFAULT NULL COMMENT '上一次登录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_account_name` (`name`),
  UNIQUE KEY `idx_account_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='账户表';

INSERT INTO `account` VALUES
(1,'admin@qq.com','admin','$2a$10$OG1zaFHT2LUy4SGcQ4EnRu9sPQMjMGEE6jARz61aQwRQ3316N6ikG','2018-01-01 00:00:00','2018-02-01 00:00:00'),
(2,'user@qq.com','user','$2a$10$yjfcoyNWgoUh3QQ3I6Lwmux57rCz3mZP1j8V4BK60EIVdwT3SkwFO','2018-01-01 00:00:00','2018-02-01 00:00:00');

DROP TABLE IF EXISTS `account_role`;

CREATE TABLE `account_role` (
  `account_id` bigint(20) unsigned NOT NULL COMMENT '账户Id',
  `role_id` bigint(20) unsigned NOT NULL COMMENT '角色Id',
  PRIMARY KEY (`account_id`,`role_id`),
  KEY `fk_ref_role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户角色表';

INSERT INTO `account_role` VALUES
(1,2),
(1,3),
(2,1);

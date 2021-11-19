DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '角色Id',
  `name` varchar(64) DEFAULT NULL COMMENT '角色名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

INSERT INTO `role` VALUES
(1,'USER'),
(2,'ADMIN'),
(3,'TEST');

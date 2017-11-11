CREATE TABLE `keywords` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL COMMENT '关键词',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


CREATE TABLE `taobao_result` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `keywords_id` bigint(20) DEFAULT NULL COMMENT '外键',
  `comment_count` int(255) DEFAULT NULL COMMENT '评论数',
  `data_str` varchar(255) DEFAULT NULL COMMENT '抓取时间',
  `detail_url` text COMMENT '店铺URL',
  `price` double DEFAULT NULL COMMENT '价格',
  `shop_name` varchar(255) DEFAULT NULL COMMENT '店铺名',
  `num` varchar(255) DEFAULT '0' COMMENT '销量',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `location` varchar(255) DEFAULT NULL COMMENT '地区',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=254 DEFAULT CHARSET=utf8;

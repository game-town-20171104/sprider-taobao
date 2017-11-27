--liquibase formatted sql
-- changeset godslhand:20171116
ALTER  TABLE  keywords add COLUMN   `active` tinyint(1) DEFAULT '1' COMMENT '是否开启' ;

-- changeset godslhand:20171116-1
ALTER  TABLE taobao_result CHANGE `data_str` spider_date datetime DEFAULT NULL COMMENT '抓取时间';

-- changeset godslhand:20171119
CREATE TABLE `shop` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` text COMMENT '店铺地址',
  `name` varchar(255) DEFAULT NULL COMMENT '店铺名称',
  `active` tinyint(1) DEFAULT '1' COMMENT '是否开启',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT '店铺信息';


CREATE TABLE `shop_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shop_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sales` int(11) DEFAULT NULL COMMENT '销量',
  `price` double DEFAULT NULL COMMENT '价格',
  `rates` int(11) DEFAULT NULL COMMENT '评论数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '店铺商品';


-- changeset godslhand:20171121
ALTER  TABLE shop_item add COLUMN  spider_date datetime DEFAULT NULL COMMENT '抓取时间';



-- changeset sworddancer:20171122
ALTER TABLE `spider`.`shop`
ADD COLUMN `type` varchar(255) AFTER `active`;



-- changeset godslhand:20171122
CREATE TABLE `search_keywords` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '关键词',
   `shop_code` varchar(255) DEFAULT NULL COMMENT '商店编号',
  `active` tinyint(1) DEFAULT '1' COMMENT '是否开启',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT '搜索关键词';

-- changeset sworddancer:20171125
ALTER TABLE `spider`.`shop_item`
ADD COLUMN `type` varchar(255) AFTER `spider_date`;



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

-- changeset godslhand:20171130
CREATE  TABLE  `reg_mail` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`username` varchar(255) DEFAULT NULL COMMENT '用户名',
`email` varchar(255) DEFAULT NULL COMMENT '邮箱',
`password` varchar(255) DEFAULT NULL COMMENT '密码',
`phone` varchar(255) DEFAULT NULL COMMENT '手机',
`success` tinyint(1) DEFAULT '0' COMMENT '是否成功',
`active` tinyint(1) DEFAULT '1' COMMENT '是否开启',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT '邮箱注册';


-- changeset godslhand:20171204
CREATE  TABLE  `reg_sony` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`psn` varchar(255) DEFAULT NULL COMMENT '帐号/邮箱',
`username` varchar(255) DEFAULT NULL COMMENT '用户名',
`password` varchar(255) DEFAULT NULL COMMENT '密码',
`country` varchar(255) DEFAULT NULL COMMENT '国家',
`province` varchar(255) DEFAULT NULL COMMENT '省',
`phone` varchar(255) DEFAULT NULL COMMENT '手机',
`question` varchar(255) DEFAULT NULL COMMENT '问题',
`ques_answer` varchar(255) DEFAULT NULL COMMENT '答案',
`step` int(11) DEFAULT NULL COMMENT '进行到的步骤',
`success` tinyint(1) DEFAULT '0' COMMENT '是否成功',
`active` tinyint(1) DEFAULT '1' COMMENT '是否开启',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT '邮箱注册';

-- changeset godslhand:20171205
ALTER  TABLE  reg_sony add COLUMN   `address` varchar(255) DEFAULT NULL  COMMENT '地址' ;
ALTER  TABLE  reg_sony add COLUMN   `city` varchar(255) DEFAULT NULL COMMENT '城市' ;
ALTER  TABLE  reg_sony add COLUMN   `first_name` varchar(255) DEFAULT NULL  COMMENT '名（老外风格）' ;
ALTER  TABLE  reg_sony add COLUMN   `last_name` varchar(255) DEFAULT NULL COMMENT '姓（老外风格）' ;

-- changeset godslhand:20171127
CREATE  TABLE  `proxy_server` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`url`  varchar(255) DEFAULT NULL COMMENT '代理服务器地址',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT '代理服务器地址';


-- changeset godslhand:20171221
UPDATE `spider`.`reg_sony` SET  `step` = step*100


-- changeset godslhand:20180103
CREATE  TABLE  `tb_cate_price` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`keyword` varchar(255) DEFAULT NULL COMMENT '关键词',
`title` varchar(255) DEFAULT NULL COMMENT '标题',
`game` varchar(255) DEFAULT NULL COMMENT '游戏名',
`shop` varchar(255) DEFAULT NULL COMMENT '掌柜',
`shop_url` varchar(255) DEFAULT NULL COMMENT '店铺URL',
`item_url` varchar(255) DEFAULT NULL COMMENT '商品地址',
`offline_name` varchar(255) DEFAULT NULL COMMENT '离线版本名称',
`offline_price` double DEFAULT NULL COMMENT '离线价格',
`auth_name` varchar(255) DEFAULT NULL COMMENT '认证版名称',
`auth_price` double DEFAULT NULL COMMENT '进行到的步骤',
`auth_half_name` varchar(255) DEFAULT NULL COMMENT '认证6个月名称',
`auth_half_price` double DEFAULT NULL COMMENT '认证6个月价格',
`un_auth_name` varchar(255) DEFAULT NULL COMMENT '非认证名称',
`un_auth_price` double DEFAULT NULL COMMENT '非认证价格',
`success` tinyint(1) DEFAULT '0' COMMENT '是否成功',
`active` tinyint(1) DEFAULT '1' COMMENT '是否开启',
`update_time` DATE DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT '分类价格';

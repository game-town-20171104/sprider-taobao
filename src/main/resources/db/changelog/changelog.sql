--liquibase formatted sql
-- changeset godslhand:20171116
ALTER  TABLE  keywords add COLUMN   `active` tinyint(1) DEFAULT '1' COMMENT '是否开启' ;

-- changeset godslhand:20171116-1
ALTER  TABLE taobao_result CHANGE `data_str` spider_date datetime DEFAULT NULL COMMENT '抓取时间';
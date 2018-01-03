package com.ylfin.spider.cateprice.vo.bean;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_cate_price")
public class CatePrice {
    private Long id;
    private String keyword;
    private String title ;
    private String game;
    private String shop;
    private String shopUrl;
    private String itemUrl;
    private String offlineName;
    private Double offlinePrice;
    private String authName;
    private Double authPrice;
    private String authHalfName;
    private Double authHalfPrice;
    private String unAuthName;
    private Double unAuthPrice;
    private Boolean active;
    private Boolean success;
    private Date updateTime ;

}

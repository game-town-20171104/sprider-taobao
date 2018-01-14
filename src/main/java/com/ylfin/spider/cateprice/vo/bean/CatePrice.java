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
    private String offlineFilter;
    private Double offlinePrice;
    private String authName;
    private String authFilter;
    private Double authPrice;
    private String authHalfName;
    private String authHalfFilter;
    private Double authHalfPrice;
    private String unAuthName;
    private String unAuthFilter;
    private Double unAuthPrice;
    private Boolean active;
    private Boolean success;
    private Date updateTime ;

}

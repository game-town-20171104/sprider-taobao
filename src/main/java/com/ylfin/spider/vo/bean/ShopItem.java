package com.ylfin.spider.vo.bean;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("shop_item")
public class ShopItem {
    private Long shopId ;
    private Integer  rates;
    private Integer sales;
    private Double price ;
    private String name ;
}

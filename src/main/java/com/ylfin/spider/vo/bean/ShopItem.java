package com.ylfin.spider.vo.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("shop_item")
public class ShopItem {
    private Long shopId ;
    @JSONField(name="totalSoldQuantity")
    private Integer  rates;

    @JSONField(name = "sold")
    private Integer sales;
    @JSONField(name = "salePrice")
    private Double price ;
    @JSONField(name ="title")
    private String name ;

    private String  spiderDate ;
}

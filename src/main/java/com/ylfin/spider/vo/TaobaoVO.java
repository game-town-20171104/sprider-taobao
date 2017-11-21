package com.ylfin.spider.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class TaobaoVO {
    private  long keywordId ;
    @JSONField(name = "nick")
    private String shop;
    @JSONField(name = "view_price")
    private String viewPrice;
    @JSONField(name = "view_fee")
    private String viewFee;
    @JSONField(name = "item_loc")
    private String area;
    @JSONField(name = "view_sales")
    private String viewSales;
    @JSONField(name = "raw_title")
    private String rawTitle;
    @JSONField(name = "comment_count")
    private String commentCount;
    @JSONField(name = "detail_url")
    private String detailUrl;
    private String startDate ;

}

package com.ylfin.spider.vo;

import com.alibaba.fastjson.annotation.JSONField;

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

    public long getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(long keywordId) {
        this.keywordId = keywordId;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getViewPrice() {
        return viewPrice;
    }

    public void setViewPrice(String viewPrice) {
        this.viewPrice = viewPrice;
    }

    public String getViewFee() {
        return viewFee;
    }

    public void setViewFee(String viewFee) {
        this.viewFee = viewFee;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getViewSales() {
        return viewSales;
    }

    public void setViewSales(String viewSales) {
        this.viewSales = viewSales;
    }

    public String getRawTitle() {
        return rawTitle;
    }

    public void setRawTitle(String rawTitle) {
        this.rawTitle = rawTitle;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }


    @Override
    public String toString() {
        return "TaobaoVO{" +
                "keywordId=" + keywordId +
                ", shop='" + shop + '\'' +
                ", viewPrice='" + viewPrice + '\'' +
                ", viewFee='" + viewFee + '\'' +
                ", area='" + area + '\'' +
                ", viewSales='" + viewSales + '\'' +
                ", rawTitle='" + rawTitle + '\'' +
                ", commentCount='" + commentCount + '\'' +
                ", detailUrl='" + detailUrl + '\'' +
                '}';
    }
}

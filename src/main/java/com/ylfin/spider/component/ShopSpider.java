package com.ylfin.spider.component;

public class ShopSpider extends BaseSpider {


    public void handle(String shopUrl){
        int currtPage = 0;


        while (true){
            String  url =shopUrl+"/i/asyncSearch.htm?spm=a1z10.3-c.w4010-2075292305.3.757df034TsdNt0&callback=jsonp148&pageNo="+(++currtPage)+"&search=y&orderType=hotsell_desc";
            driver.get(url);
            //c-price
            //sale-num
            //item-name
        }
    }
}

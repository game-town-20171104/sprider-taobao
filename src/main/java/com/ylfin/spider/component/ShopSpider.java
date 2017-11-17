package com.ylfin.spider.component;

public class ShopSpider extends BaseSpider {


    public void handle(String shopUrl){
        int page = 0;
        String  url =shopUrl+"/search.htm?spm=a1z10.3-c.w4010-2075292305.3.757df034TsdNt0&search=y&orderType=hotsell_desc";
        driver.get(url);
//        driver.
        while (true){
//            driver.findElements();

        }
    }
}

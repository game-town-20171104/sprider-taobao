package com.ylfin.spider.component;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaoBaoHelper {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private BaseSpider spider;

    public TaoBaoHelper(BaseSpider spider) {
        this.spider = spider;
    }

    public void search(String keyword){
        String searchUrl = "https://www.taobao.com";
        spider.getDriver().get(searchUrl);
        spider.waitFindElementByClass("search-combobox-input-wrap").click();
        WebElement input = spider.waitFindElementByClass("search-combobox-input");
        spider.sendUnFocusAbleKey(input, keyword);
        WebElement btn = spider.waitFindElementByClass("btn-search");
        spider.simpleRandomWaite(2);
        btn.click();
        logger.info("搜索完毕");
    }
}

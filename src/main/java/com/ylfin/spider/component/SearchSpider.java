package com.ylfin.spider.component;

import com.ylfin.spider.utils.SpiderUtils;
import com.ylfin.spider.vo.bean.SearchKeyWords;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;

public class SearchSpider extends BaseSpider {
    private String ipServer = "http://whatismyip.akamai.com";
    Logger logger = LoggerFactory.getLogger(getClass());

    public void handle(SearchKeyWords searchKeyWords) {
        String lastIp = "";

        driver.get(ipServer);
        String ip = driver.findElement(By.tagName("body")).getText();

        if (!StringUtils.isEmpty(lastIp) && lastIp.equals(ip)) {
            return;
        }
        logger.info("ip 检测完毕");
        String searchUrl = "https://www.taobao.com";
        driver.get(searchUrl);
        //search-combobox-input
        this.waitFindElementByClass("search-combobox-input-wrap").click();
        WebElement input = this.waitFindElementByClass("search-combobox-input");
        this.sendUnFocusAbleKey(input, searchKeyWords.getName());
        WebElement btn = this.waitFindElementByClass("btn-search");
        btn.click();
        logger.info("搜索完毕");
        this.scrollSlow2end();
        this.scrollBack2Top();
        WebElement sortBtn = this.waitFindElement(By.xpath("//*[@data-value='sale-desc']"));
        sortBtn.click();
        this.scrollSlow2end();
        this.scrollBack2Top();
        logger.info("查看列表完毕");
        this.waitFindElementByClass("location").click();
        this.waitFindElementByClass("guess").click();
        this.simpleWaite(400L);
        this.waitFindElement(By.xpath("//*[@data-nid='" + searchKeyWords.getShopCode() + "']")).click();
        logger.info("筛选找到商品");
        this.scrollSlow2end();
        this.scrollBack2Top();
        logger.info("详情查看完毕");
        this.waitFindElement(By.xpath("//*[@shortcut-label='查看累计评论']")).click();
        this.scrollSlow2end();
        this.scrollBack2Top();
        logger.info("评论查看完毕");
        this.waitFindElementByClass("tb-shop-name").click();
        this.scrollSlow2end();
        ((JavascriptExecutor) driver).executeScript("window.scrollTo( 0,200)");
        logger.info("店铺排行查看完毕");
        List<WebElement> elements = this.waitFindElementsByClass("photo");
        int index = SpiderUtils.randomInteger(1, elements.size());
        elements.get(index).click();
        driver.getWindowHandles();
        logger.info("随机查看商品");
        this.closeOthers();
    }


    public static void main(String[] args) {
        SearchKeyWords searchKeyWords = new SearchKeyWords();
        searchKeyWords.setId(1L);
        searchKeyWords.setName("PS4 数字 勇者斗恶龙11 认证");
        searchKeyWords.setShopCode("561426363500");
        SearchSpider spider = new SearchSpider();
        spider.initWithProfile();
        spider.handle(searchKeyWords);

    }


}

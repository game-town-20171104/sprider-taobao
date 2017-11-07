package com.ylfin.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ylfin.spider.vo.TaobaoVO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.util.List;

public class SeleniumSpider extends BaseSpider {
    private static String keyword = "刺客信条 起源 ps4";

    public static void main(String[] args) {
        SeleniumSpider spider = new SeleniumSpider();
        spider.init();
        spider.jsonHandle(keyword);
        spider.quit();
    }

    public void handle(String keyword) {


        driver.get("https://s.taobao.com/");
        String title = driver.getTitle();
        String fileName = keyword + "-" + LocalDate.now() + ".csv";
        System.out.println(title);
        WebElement btn = driver.findElement(By.xpath("//*[@class='btn-search']"));
        WebElement search = driver.findElement(By.xpath("//input[@class='search-combobox-input']"));
        search.sendKeys(keyword);
        btn.submit();
        this.waiteTitleCondition(keyword);
        WebElement sortBtn = driver.findElement(By.xpath("//*[@data-value='sale-desc']"));
        this.waitAndClick(sortBtn);
        List<WebElement> items = driver.findElements(By.xpath("//*[@data-category='auctions']"));
        this.waitBy(By.xpath("//*[@data-category='auctions']"));
        for (WebElement element : items) {
            this.waitBy(By.xpath(".//*[contains(@class,'price')]/strong"));
            String money = element.findElement(By.xpath(".//*[contains(@class,'price')]/strong")).getText();
            String num = element.findElement(By.xpath(".//*[@class='deal-cnt']")).getText();
            String name = element.findElement(By.xpath(".//*[@class='J_ClickStat']")).getText();
            String shopname = element.findElement(By.xpath(".//*[contains(@class,'shopname')]")).getText();
            String location = element.findElement(By.xpath(".//*[@class='location']")).getText();
            System.out.println(String.format("%s,%s,%s,%s,%s", shopname, location, money, name, num));
        }


    }


    public void jsonHandle(String keyword) {
        int pageSize = 44;
        int curPage = 0;
        while (true) {
            String url = "https://s.taobao.com/search?data-key=s&data-value=" + (curPage * pageSize) + "&ajax=true&_ksTS=1510048516809_1374&callback=jsonp1514&initiative_id=staobaoz_20171107&q=" + keyword + "&sort=sale-desc&bcoffset=0&p4ppushleft=%2C44&s=44";
            driver.get(url);
            String content = driver.getPageSource();
            int length =content.indexOf("({");
            String json = content.substring(length + 1);
            json = json.substring(json.length()-2);
            JSONArray items = JSON.parseObject(json).getJSONObject("mods").getJSONObject("itemlist").getJSONObject("data").getJSONArray("auctions");
           List<TaobaoVO>  taobaoVOS =JSON.parseArray(items.toJSONString(), TaobaoVO.class);
            if(taobaoVOS.size()==0)
                break;
        }

    }


}

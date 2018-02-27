package com.ylfin.spider.eshop;

import com.ylfin.spider.component.BaseSpider;
import com.ylfin.spider.eshop.service.EshopService;
import com.ylfin.spider.eshop.vo.bean.EshopBean;
import com.ylfin.spider.register.Register;
import lombok.extern.java.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.*;


@Log
public class EshopSpider extends BaseSpider implements Register {


    private EshopService eshopService;

    public EshopSpider(EshopService eshopService) {
       this.eshopService = eshopService;
    }

    @Override
    public void initRegister() {
        super.setHeadless(false);
        super.init();
    }

    @Override
    public void handle(Object o) {
        this.getOverdue(10,"https://eshop-prices.com/?currency=CNY");
        Date startDate = new Date();
        String html = this.getDriver().getPageSource();
        Document document = Jsoup.parse(html);
//        Elements elements= document.getElementsByTag("table");
//        elements.get(0).cssSelector("")
        List<String> countrys = new ArrayList<>();
        Long start = System.currentTimeMillis();
        document.select("thead th[title]").forEach(ce ->countrys.add(ce.text()));
//        List<List<EshopBean>> esbs = new ArrayList<>();
        List<EshopBean> eshopBeans = new ArrayList<>();
        document.select("tr[data-table-searchable]").forEach(game -> {

           Elements tags = game.children();
           if(tags.size()-1!=countrys.size()){
               System.out.println(tags.text());
               System.out.println(countrys.size());
               throw new RuntimeException("数据不一致 tags:"+tags.size()+" countrys:"+countrys.size());
           }
           String  title = tags.get(0).text();
            for (int i = 1; i < tags.size(); i++) {
                Element element = tags.get(i);
                EshopBean esb = new EshopBean();
                esb.setCountry(countrys.get(i-1));
                esb.setGame(title);
                esb.setSpiderTime(startDate);
                try {
                    esb.setPrice(Double.valueOf(element.text().substring(1)));
                } catch (NumberFormatException e) {
                   log.info(e.toString());
                }
                eshopBeans.add(esb);
            }
//            esbs.add(eshopBeans);

        });
        log.info("解析时间："+(System.currentTimeMillis()-start)/1000+"秒");
        eshopService.batchSave(eshopBeans);

    }

    private void seleniumHandle(){
        List<WebElement> celements = this.waitFindElements(By.xpath("//th[@title]"));
        List<String> countrys = new ArrayList<>();
        celements.forEach(ce -> countrys.add(ce.getText()));
        List<WebElement> games = this.waitFindElements(By.xpath("//tr[@data-table-searchable]"));
        List<List<EshopBean>> esbs = new ArrayList<>();
        games.forEach(game -> {
            List<EshopBean> eshopBeans = new ArrayList<>();
            String title = game.findElement(By.tagName("th")).getText();
            List<WebElement> prices = game.findElements(By.tagName("td"));
            if (prices.size() != countrys.size()) {
                throw new RuntimeException("数据不一致");
            }
            for (int i = 0; i < prices.size(); i++) {
                WebElement price = prices.get(i);
                EshopBean esb = new EshopBean();
                String priceStr = price.getText().substring(1);
                esb.setGame(title);
//                System.out.println(priceStr);
                try {
                    esb.setPrice(Double.valueOf(priceStr));
                } catch (NumberFormatException e) {
                    log.info(e.toString());
                    System.out.println(priceStr);
                }
                esb.setCountry(countrys.get(i));
                eshopBeans.add(esb);
            }
            esbs.add(eshopBeans);
        });

    }

    @Override
    public void close() {
        super.quit();
    }


    public static void main(String[] args) {
//        EshopSpider eshopSpider = new EshopSpider();
//        eshopSpider.initRegister();
//        eshopSpider.handle(null);
//        eshopSpider.quit();
    }
}

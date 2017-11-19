package com.ylfin.spider.component;

import com.ylfin.spider.utils.SpiderUtils;
import com.ylfin.spider.vo.bean.ShopItem;
import com.ylfin.spider.vo.bean.Shop;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ShopSpider extends BaseSpider {

    Logger logger = LoggerFactory.getLogger(getClass());

    public void handle(Shop shop) {
        int curPage = 0;
        int spiderPage =0;
        int total = 5;
        while (true) {
            if (curPage >= total)
                break;
            if (spiderPage>0&&curPage >= spiderPage)
                break;
            //i/async
            String url = shop.getUrl() + "/Search.htm?callback=jsonp148&pageNo=" + (++curPage) + "&search=y&orderType=hotsell_desc";
            try {
                driver.get(url);
                simpleWaite(1*1000L);
                if(spiderPage ==0){
                    String pageLable =this.waitFindElementByClass("page-info").getText();
                    spiderPage =Integer.valueOf(pageLable.split("/")[1]);
                    String totalElement =this.waitFindElementByClass("search-result").getText();
                    logger.info(totalElement);
                }


                logger.info(String.format("%s==>总页数：%s，配置页数：%s/%s", shop, spiderPage, curPage, total));
                List<WebElement> prices = this.waitFindElementsByClass("c-price");
                List<WebElement> sales = this.waitFindElementsByClass("sale-num");
                List<WebElement> items = this.waitFindElementsByClass("item-name");
                List<WebElement> rates = this.waitFindElementsByClass("rates");
                if (prices != null && sales != null && items != null & rates != null
                        && prices.size() == sales.size() && prices.size() == items.size() && prices.size() == rates.size()) {
                    List<ShopItem> itemVOS = new ArrayList<>();
                    for (int i = 0; i < prices.size(); i++) {
                        ShopItem vo = new ShopItem();
                        vo.setPrice(Double.valueOf(prices.get(i).getText()));
                        vo.setSales(Integer.valueOf(sales.get(i).getText()));
                        String rateStr = rates.get(i).getText();
                        vo.setRates(Integer.valueOf(SpiderUtils.getNum(rateStr)));
                        vo.setName(items.get(i).getText());
                        itemVOS.add(vo);
                    }


                } else {
                    logger.error("出错，抓去到的价格、数量、商品名称、信息长度不一致");
                    break;
                }
            } catch (Exception e) {
                logger.error("爬虫抓取出错",e);
               continue;
            }


        }
    }


    public static void main(String[] args) {
        String shopUrl = "http://langyuedianzi.taobao.com";
        Shop shop = new Shop();
        shop.setId(1L);
        shop.setUrl(shopUrl);
        shop.setName("朗悦数码");

        ShopSpider shopSpider = new ShopSpider();
        shopSpider.init();
        try {
            shopSpider.handle(shop);
        } catch (Exception e) {
            e.printStackTrace();
        }
        shopSpider.quit();

    }
}

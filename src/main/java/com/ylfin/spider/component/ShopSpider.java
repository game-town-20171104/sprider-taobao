package com.ylfin.spider.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ylfin.spider.service.ShopItemService;
import com.ylfin.spider.utils.SpiderUtils;
import com.ylfin.spider.utils.StringUtils;
import com.ylfin.spider.vo.bean.ShopItem;
import com.ylfin.spider.vo.bean.Shop;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ShopSpider extends BaseSpider {

    private ShopItemService shopItemService;

    Logger logger = LoggerFactory.getLogger(getClass());
    private String startDate;
    private int total ;
    private boolean first =true;

    public String getStartDate() {
        return startDate;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public ShopSpider(ShopItemService shopItemService) {
        this.shopItemService = shopItemService;
    }

    public void handle(Shop shop) {
        int curPage = 0;
        int spiderPage = 0;

        if(first){

            driver.get("https://login.m.taobao.com/login.htm");
            WebElement input =this.waitFindElementByClass("am-input-required");
            String username =input.getText();
            System.out.println(username);
            username= input.getAttribute("value");
            System.out.println(username);
            if(!org.springframework.util.StringUtils.isEmpty(username)){
                this.waitFindElementByClass("am-button-submit").click();
            }
            simpleWaite(2000L);
            this.waiteTitleCondition("我的淘宝");
            simpleWaite(200L);
        }

        first= false;
        while (true) {
            if (spiderPage==0&&curPage >= total)
                break;
            if (spiderPage > 0 && curPage >= spiderPage)
                break;



            //i/async
//            String url = shop.getUrl() + "/i/asynSearch.htm?callback=jsonp148&path=/search.htm&mid=w-1797666998-0&wid=1797666998&pageNo=" + (++curPage) + "&search=y&orderType=hotsell_desc";
            String url = shop.getUrl();

            try {
                driver.get(url);

//              String json = this.loadPage(url,0L);
//                simpleWaite(1*1000L);
                ((JavascriptExecutor) driver).executeScript(getJavaScript(++curPage));
                this.simpleWaite(Long.valueOf(SpiderUtils.randomInteger(200,500)));
                String jsonp = this.waitFindElement(By.id("jsonData")).getText();
                JSONObject res = JSON.parseObject(jsonp);
                Integer totalNum = res.getJSONObject("data").getInteger("totalResults");
                if (spiderPage == 0) {
                    spiderPage = totalNum / 30 + 1;
                }

                logger.info(String.format("%s==>总页数：%s，配置页数：%s/%s", shop, spiderPage, curPage, total));
                System.out.println(res.getJSONObject("data").getJSONArray("itemsArray"));
                JSONArray jsonArray = res.getJSONObject("data").getJSONArray("itemsArray");
                System.out.println(jsonArray);
                List<ShopItem> shopItems = JSON.parseArray(jsonArray.toJSONString(), ShopItem.class);

                for (ShopItem shopItem : shopItems) {
                    shopItem.setSpiderDate(startDate);
                    shopItem.setShopId(shop.getId());
                }
                shopItemService.batchInsert(shopItems);
                simpleWaite(Long.valueOf(SpiderUtils.randomInteger(500,1100)));
            } catch (Exception e) {
                logger.error("爬虫抓取出错", e);
                continue;
            }


        }
    }


    private String getJavaScript(int curPage) {
        String js = "\n" +
                "    // 请求页面数据, 开始解析\n" +
                "    function getPageList() {\n" +
                "        // 从 location 获取页面参数\n" +
                "        var params = [],\n" +
                "                PAGE_QUERY = {},\n" +
                "                VIEW_DATA = {};\n" +
                "        var page = window.G_msp_path ? window.G_msp_path : '';//兼容店铺首页\n" +
                "        var search = location.search;\n" +
                "        var data ={\n" +
                "              \"currentPage\":" + curPage + ",\n" +
                "               \"pageSize\":\"30\",\n" +
                "               \"sort\":'hotsell',\n" +
                "               \"q\":\"\"\n" +
                "            };\n" +
                "        if (search) {\n" +
                "            //search = decodeURIComponent(search);\n" +
                "            search = search.slice(1).split('&');\n" +
                "            search.forEach(function (param) {\n" +
                "                param = param.split('=');\n" +
                "                var k = param[0];\n" +
                "                var v = param[1];\n" +
                "                try {\n" +
                "                    v = decodeURIComponent(v);\n" +
                "\n" +
                "                } catch (err) {\n" +
                "                }\n" +
                "                if (k == 'page') {\n" +
                "                    page = v;\n" +
                "                } else {\n" +
                "\n" +
                "                    params.push(k + ':' + v);\n" +
                "                    PAGE_QUERY[k] = v;\n" +
                "                }\n" +
                "                if(k=='shop_id'){\n" +
                "                 \n" +
                "                  data['shopId']=v;\n" +
                "                }\n" +
                "            })\n" +
                "        }\n" +
                "        if (!PAGE_QUERY.userId && window.G_msp_userId) {\n" +
                "            PAGE_QUERY.userId = window.G_msp_userId;\n" +
                "            VIEW_DATA.userId = window.G_msp_userId;\n" +
                "            params.push('userId:' + window.G_msp_userId);\n" +
                "            //兼容店铺首页\n" +
                "        }\n" +
                "        ;\n" +
                "        if (window.G_msp_shopId) {\n" +
                "            params.push('shop_id:' + window.G_msp_shopId);\n" +
                "             data['shopId']= window.G_msp_shopId;\n" +
                "        }\n" +
                "        ;\n" +
                "        \n" +
                "\n" +

                "        lib.mtop.request({\n" +
                "            api: \"com.taobao.search.api.getShopItemList\",\n" +
                "            v: \"2.0\",\n" +
                "            data: data\n" +
                "        }, function (resp) {\n" +
                "            var resp = resp;\n" +
                "            var jsondata = document.getElementById(\"jsonData\");\n" +
                "            if(!jsondata){\n" +
                "               jsondata =document.createElement(\"div\");\n" +
                "                jsondata.setAttribute(\"id\",\"jsonData\");\n" +
                "                document.body.appendChild(jsondata);\n" +
                "            }\n" +
                "           jsondata.innerHTML=JSON.stringify(resp);\n" +
                "        }, function (resp) {\n" +
                "            console.log('获取页面数据失败: ', resp);\n" +
                "        });\n" +
                "    }\n" +
                "\n" +
                "    getPageList();\n";
        return js;
    }

    public static void main(String[] args) {
        //https://langyuedianzi.taobao.com/i/asynSearch.htm?_ksTS=1511148646595_209&callback=jsonp210&mid=w-1797666998-0&wid=1797666998&path=/search.htm&search=y&pageNo=2
        String shopUrl = "https://shop34271480.m.taobao.com/?shop_id=34271480&user_id=53705938#list";
        Shop shop = new Shop();
        shop.setId(1L);
        shop.setUrl(shopUrl);
        shop.setName("朗悦数码");

        ShopSpider shopSpider = new ShopSpider(null);
//        shopSpider.setDeviceName("iPhone 6");
        shopSpider.init();
        try {
            shopSpider.handle(shop);
        } catch (Exception e) {
            e.printStackTrace();
        }
        shopSpider.quit();

    }
}

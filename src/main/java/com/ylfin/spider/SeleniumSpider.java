package com.ylfin.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ylfin.spider.utils.CSVTools;
import com.ylfin.spider.vo.TaobaoVO;
import org.openqa.selenium.By;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeleniumSpider extends BaseSpider {

    public static void main(String[] args) {

        List<String> keywords = new ArrayList<>();
        keywords.add("刺客信条 起源 ps4");


        SeleniumSpider spider = new SeleniumSpider();
        spider.init();
        for (String keyword : keywords) {
            spider.jsonHandle(keyword);
        }

        spider.quit();
    }


    public void jsonHandle(String keyword) {
        int pageSize = 44;//不可编辑
        int curPage = 0;
        int total = 9;//爬取总页数
        List<TaobaoVO> totals = new ArrayList<>();
        String name = keyword + "-" + LocalDate.now() + ".csv";
        while (true) {

            String url = "https://s.taobao.com/search?data-key=s&data-value=" + (curPage++ * pageSize) + "&ajax=true&_ksTS=1510048516809_1374&callback=jsonp1514&initiative_id=staobaoz_20171107&q=" + keyword + "&sort=sale-desc&bcoffset=0&p4ppushleft=%2C44&s=44";
            driver.get(url);
            String content = driver.findElement(By.tagName("pre")).getText();
            String json = unwarpJSONP(content);
            JSONArray items = JSON.parseObject(json).getJSONObject("mods").getJSONObject("itemlist").getJSONObject("data").getJSONArray("auctions");
            List<TaobaoVO> taobaoVOS = JSON.parseArray(items.toJSONString(), TaobaoVO.class);
            totals.addAll(taobaoVOS);
            if (taobaoVOS.size() == 0 || curPage >= total) {
                break;
            }

        }
        String allstr = JSON.toJSONString(totals);
        CSVTools.write(JSON.parseArray(allstr, Map.class), name);

    }


    private String unwarpJSONP(String jsonp) {
        int length = jsonp.indexOf("({");
        String json = jsonp.substring(length + 1);
        json = json.substring(0, json.length() - 2);
        return json;
    }

}

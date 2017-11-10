package com.ylfin.spider.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ylfin.spider.service.TaoBaoResultService;
import com.ylfin.spider.utils.CSVTools;
import com.ylfin.spider.vo.TaobaoVO;
import com.ylfin.spider.vo.bean.KeyWords;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeleniumSpider extends BaseSpider {
    Logger logger = LoggerFactory.getLogger(getClass());
    private int total =9;

    private TaoBaoResultService taoBaoResultService;

    public SeleniumSpider(TaoBaoResultService taoBaoResultService) {
        this.taoBaoResultService = taoBaoResultService;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static void main(String[] args) {
//
//        List<KeyWords> keywords = new ArrayList<>();
//        keywords.add(new KeyWords(1L,"刺客信条 起源 ps4"));
//
//
//        SeleniumSpider spider = new SeleniumSpider();
//        spider.init();
//        for (KeyWords keyword : keywords) {
////            spider.jsonHandle(keyword);
//        }
//
//        spider.quit();
    }


    public void jsonHandle(KeyWords keyword) {
        int pageSize = 44;//不可编辑
        int curPage = 0;
//        List<TaobaoVO> totals = new ArrayList<>();
        String name = keyword + "-" + LocalDate.now() + ".csv";
        while (true) {
           if(curPage >= total){
               break;
           }
            String url = "https://s.taobao.com/search?data-key=s&data-value=" + (curPage++ * pageSize) + "&ajax=true&_ksTS=1510048516809_1374&callback=jsonp1514&initiative_id=staobaoz_20171107&q=" + keyword + "&sort=sale-desc&bcoffset=0&p4ppushleft=%2C44&s=44";
            driver.get(url);
            String content = driver.findElement(By.tagName("pre")).getText();
            String json = unwarpJSONP(content);
            JSONObject result =JSON.parseObject(json);
            JSONObject page =result.getJSONObject("mods").getJSONObject("pager");
            String totalPage =page.getJSONObject("data").getString("totalPage");
            System.out.println(String.format("%s==>总页数：%s，配置页数：%s/%s",keyword,totalPage,curPage,total));
            List<TaobaoVO> taobaoVOS = null;
            try {
                JSONArray items = result.getJSONObject("mods").getJSONObject("itemlist").getJSONObject("data").getJSONArray("auctions");
                taobaoVOS = JSON.parseArray(items.toJSONString(), TaobaoVO.class);
            } catch (Exception e) {
                logger.error("转换出错",e);
            }
//            totals.addAll(taobaoVOS);
            if (CollectionUtils.isEmpty(taobaoVOS)) {
                logger.info("本页数据为空");
                break;
            }

            for (TaobaoVO taobaoVO:taobaoVOS){
                taobaoVO.setKeywordId(keyword.getId());
                String num =getNum(taobaoVO.getViewSales());
                taobaoVO.setViewSales(num);
                if(StringUtils.isEmpty(taobaoVO.getCommentCount())){
                    taobaoVO.setCommentCount("0");
                }
            }

            if(taobaoVOS!=null&&taoBaoResultService!=null){
                taoBaoResultService.batchSave(taobaoVOS);
            }

        }
        logger.info("关键词:{}已经读取完",keyword);
//        String allstr = JSON.toJSONString(totals);
//        CSVTools.write(JSON.parseArray(allstr, Map.class), "中文"+name);

    }
    private String  getNum(String  viewNum){

        return viewNum.replaceAll("人付款","").replaceAll("人收货","");
    }

    private String unwarpJSONP(String jsonp) {
        int length = jsonp.indexOf("({");
        String json = jsonp.substring(length + 1);
        json = json.substring(0, json.length() - 2);
        return json;
    }

}

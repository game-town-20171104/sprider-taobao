package com.ylfin.spider.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ylfin.spider.service.TaoBaoResultService;
import com.ylfin.spider.utils.CSVTools;
import com.ylfin.spider.utils.DateUtils;
import com.ylfin.spider.utils.SpiderUtils;
import com.ylfin.spider.vo.TaobaoVO;
import com.ylfin.spider.vo.bean.KeyWords;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;

public class SeleniumSpider extends BaseSpider {
    Logger logger = LoggerFactory.getLogger(getClass());
    private int total = 9;
    private TaoBaoResultService taoBaoResultService;
    private String startDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

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
        SeleniumSpider spider = new SeleniumSpider(null);
        spider.init();
        spider.quit();
    }


    public void jsonHandle(KeyWords keyword) {
        int pageSize = 44;//不可编辑
        int curPage = 0;
        int totalPage = 0;

        while (true) {
            if (curPage >= total) {
                break;
            }
            if (totalPage > 0 && curPage >= totalPage) {
                break;
            }
            driver.get("https://www.taobao.com");
           simpleWaite(1000L);
            List<TaobaoVO> taobaoVOS = null;
            String initative_id="staobaoz_"+ DateUtils.format("yyyyMMdd");
            String _ksTS=System.currentTimeMillis()+"_"+SpiderUtils.randomInteger(100,999);
            //jsonp980
            //https://s.taobao.com/search?data-key=s%2Cps&data-value=0%2C1&ajax=true&_ksTS=1515897442531_979&callback=jsonp980&initiative_id=staobaoz_20180114&q=ps4+%E7%A5%9E%E6%B5%B74&bcoffset=4&ntoffset=4&p4ppushleft=2%2C48&s=44
            String url = "https://s.taobao.com/search?data-key=s&data-value=" + (curPage++ * pageSize) + "&ajax=true&_ksTS="+_ksTS+"&callback=jsonp"+new Random().nextInt(9999)+"&initiative_id="+initative_id+"&q=" + keyword.getTitle() + "&sort=default&bcoffset=0&p4ppushleft=%2C48&s=44";
            // 综合排序：default   ；  人气：renqi-desc ；销售量：sale-desc
            logger.info(url);
            String json = this.loadPage(url,0L);
            if (StringUtils.isEmpty(json)) {
                logger.info(keyword + "==" + curPage + "采集到数据为空，重新加载，并等候1s");
                this.loadPage(url,1000L);
            }
            try {
                JSONObject result = JSON.parseObject(json);
                JSONObject page = result.getJSONObject("mods").getJSONObject("pager");
                if (totalPage == 0) {
                    if (page.containsKey("data")) {
                        totalPage = page.getJSONObject("data").getInteger("totalPage");
                    } else {
                        totalPage =1;

                    }

                }
                String tips=result.getJSONObject("mods").getJSONObject("tips").getString("status");
                if("show".equals(tips)){
                    logger.info("%s==>为查询到相关的数据，返回结果为推荐数据，跳过！！");
                    break;
                }
                logger.info(String.format("%s==>总页数：%s，配置页数：%s/%s", keyword, totalPage, curPage, total));
                JSONObject itemlist = result.getJSONObject("mods").getJSONObject("itemlist");
                if (!itemlist.containsKey("data")) {
                    logger.info("本页数据为空,已到最后一页，或结果查询无数据");
                    break;
                }
                JSONArray items = itemlist.getJSONObject("data").getJSONArray("auctions");
                taobaoVOS = new ArrayList<>();
                for (int i=0;i<items.size();i++){
                   JSONObject job = items.getJSONObject(i);
                   if(job.containsKey("isHideIM")&&job.getBoolean("isHideIM")){
                     continue;
                   }
                    taobaoVOS.add(JSON.parseObject(job.toJSONString(),TaobaoVO.class));
                }
//                taobaoVOS = JSON.parseArray(items.toJSONString(), TaobaoVO.class);
            } catch (Exception e) {
                logger.error(keyword + "转换出错", e);
                logger.info("出错json：{}", json);
                continue;
            }
            if (CollectionUtils.isEmpty(taobaoVOS)) {
                logger.info("本页数据为空");
                continue;
            }

            for (TaobaoVO taobaoVO : taobaoVOS) {
                taobaoVO.setKeywordId(keyword.getId());
                String num = SpiderUtils.getNum(taobaoVO.getViewSales());
                taobaoVO.setViewSales(num);
                taobaoVO.setStartDate(startDate);
                if (StringUtils.isEmpty(taobaoVO.getCommentCount())) {
                    taobaoVO.setCommentCount("0");
                }
            }

            if (taobaoVOS != null && taoBaoResultService != null) {
                taoBaoResultService.batchSave(taobaoVOS);
            }

        }
        logger.info("关键词:{}已经读取完", keyword);

    }






}

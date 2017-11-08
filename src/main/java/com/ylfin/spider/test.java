package com.ylfin.spider;


import com.alibaba.fastjson.JSON;
import com.ylfin.spider.vo.TaobaoVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by clz on 2017/11/4.
 */
public class test {

    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        System.out.println("Current Date="+today);

        List<TaobaoVO> vos = new ArrayList<>();
        TaobaoVO t = new TaobaoVO();
        t.setNick("cc");
        t.setArea("北京");
        vos.add(t);
        String str ="{\n" +
                "            \"i2iTags\": {\n" +
                "              \"samestyle\": {\n" +
                "                \"url\": \"\"\n" +
                "              },\n" +
                "              \"similar\": {\n" +
                "                \"url\": \"/search?type\\u003dsimilar\\u0026app\\u003di2i\\u0026rec_type\\u003d1\\u0026uniqpid\\u003d\\u0026nid\\u003d553668791920\"\n" +
                "              }\n" +
                "            },\n" +
                "            \"p4pTags\": [],\n" +
                "            \"nid\": \"553668791920\",\n" +
                "            \"category\": \"50012840\",\n" +
                "            \"pid\": \"\",\n" +
                "            \"title\": \"\\u003cspan class\\u003dH\\u003ePS\\u003c/span\\u003e\\u003cspan class\\u003dH\\u003e4\\u003c/span\\u003e游戏 \\u003cspan class\\u003dH\\u003e刺客\\u003c/span\\u003e\\u003cspan class\\u003dH\\u003e信条\\u003c/span\\u003e7 \\u003cspan class\\u003dH\\u003e起源\\u003c/span\\u003e 中文版附特典 普通/豪华版 现货\",\n" +
                "            \"raw_title\": \"PS4游戏 刺客信条7 起源 中文版附特典 普通/豪华版 现货\",\n" +
                "            \"pic_url\": \"//g-search3.alicdn.com/img/bao/uploaded/i4/i2/91913214/TB2r2uac2kmyKJjSZFmXXX1EFXa_!!91913214.jpg\",\n" +
                "            \"detail_url\": \"//item.taobao.com/item.htm?id\\u003d553668791920\\u0026ns\\u003d1\\u0026abbucket\\u003d1#detail\",\n" +
                "            \"view_price\": \"298.00\",\n" +
                "            \"view_fee\": \"8.00\",\n" +
                "            \"item_loc\": \"上海\",\n" +
                "            \"view_sales\": \"1755人收货\",\n" +
                "            \"comment_count\": \"918\",\n" +
                "            \"user_id\": \"91913214\",\n" +
                "            \"nick\": \"jxlyh91\",\n" +
                "            \"shopcard\": {\n" +
                "              \"levelClasses\": [\n" +
                "                {\n" +
                "                  \"levelClass\": \"icon-supple-level-jinguan\"\n" +
                "                }\n" +
                "              ],\n" +
                "              \"isTmall\": false,\n" +
                "              \"delivery\": [\n" +
                "                491,\n" +
                "                1,\n" +
                "                5779\n" +
                "              ],\n" +
                "              \"description\": [\n" +
                "                495,\n" +
                "                1,\n" +
                "                7806\n" +
                "              ],\n" +
                "              \"service\": [\n" +
                "                495,\n" +
                "                1,\n" +
                "                7245\n" +
                "              ],\n" +
                "              \"encryptedUserId\": \"UOFHSvFvyvFQT\",\n" +
                "              \"sellerCredit\": 16,\n" +
                "              \"totalRate\": 9985\n" +
                "            },\n" +
                "            \"icon\": [\n" +
                "              {\n" +
                "                \"title\": \"店铺红包\",\n" +
                "                \"dom_class\": \"icon-fest-2017taobaodianpuhong\",\n" +
                "                \"position\": \"3\",\n" +
                "                \"show_type\": \"0\",\n" +
                "                \"icon_category\": \"baobei\",\n" +
                "                \"outer_text\": \"0\",\n" +
                "                \"html\": \"\",\n" +
                "                \"icon_key\": \"icon-fest-2017taobaodianpuhong\",\n" +
                "                \"trace\": \"srpservice\",\n" +
                "                \"traceIdx\": 0,\n" +
                "                \"innerText\": \"2017淘宝嘉年华店铺红包\"\n" +
                "              }\n" +
                "            ],\n" +
                "            \"comment_url\": \"//item.taobao.com/item.htm?id\\u003d553668791920\\u0026ns\\u003d1\\u0026abbucket\\u003d1\\u0026on_comment\\u003d1\",\n" +
                "            \"shopLink\": \"//store.taobao.com/shop/view_shop.htm?user_number_id\\u003d91913214\",\n" +
                "            \"risk\": \"\"\n" +
                "          }";


        String jsonStr =JSON.toJSONString(vos);
        System.out.println( JSON.parseArray(jsonStr, HashMap.class));






































    }
}


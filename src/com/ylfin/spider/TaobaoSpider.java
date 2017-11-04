package com.ylfin.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaobaoSpider {


    public void handle(String keywords) throws Exception {
        String url = "https://s.taobao.com/";
        HtmlPage page = null;
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
//        new InterceptWebConnection(webClient);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

//        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setTimeout(30000);
        page = webClient.getPage(url);

        final HtmlButton button = (HtmlButton) page.getFirstByXPath("//*[@class='btn-search']");
        final HtmlTextInput textField = (HtmlTextInput) page.getFirstByXPath("//input[@class='ks-combobox-input']");

        // Change the value of the text field
        String name = keywords + "-"+LocalDate.now() + ".csv";
        textField.setValueAttribute(keywords);
        page = button.click();
        List<HtmlDivision> items = page.getByXPath("//*[@class='item']");
        String content = page.asXml();
        CSVTools.write(this.getByJSON(content), name);
//        webClient.waitForBackgroundJavaScript(1000*5);
//        List<Map<String,String>> results =new ArrayList<Map<String, String>>();
//
//        for(HtmlDivision div:items){
//            Map<String,String> item= new HashMap<String, String>();
//           String money = ((HtmlElement)div.getFirstByXPath("//*[contains(@class,'price')]/strong")).getTextContent();
//           String num = ((HtmlElement)div.getFirstByXPath("//*[@class='deal-cnt']")).getTextContent();
//           String name = ((HtmlElement)div.getFirstByXPath("//*[@class='J_ClickStat']")).getTextContent();
//           String shopname  = ((HtmlElement)div.getFirstByXPath("//*[@class='shopname']")).getTextContent();
//           String location  = ((HtmlElement)div.getFirstByXPath("//*[@class='location']")).getTextContent();
//            item.put("shopname",shopname);
//            item.put("location",location);
//            item.put("money",money);
//            item.put("name",name);
//            item.put("num",num);
//            System.out.println(item);
//            results.add(item);
//        }
//        System.out.println(results.size());
    }

    public static void main(String[] args) throws Exception {
        String keywords = "刺客信条 起源 ps4";

        TaobaoSpider taobaoSpider = new TaobaoSpider();
        taobaoSpider.handle(keywords);

    }


    private List<Map> getByJSON(String content) {
        int length = content.indexOf("g_page_config");
        String json = content.substring(length + 16);
        json = json.split("};")[0] + "}";
        JSONArray items = JSON.parseObject(json).getJSONObject("mods").getJSONObject("itemlist").getJSONObject("data").getJSONArray("auctions");
        List<Map> results = new ArrayList<Map>();

        for (int i = 0; i < items.size(); i++) {
            JSONObject div = items.getJSONObject(i);
            Map<String, String> item = new HashMap<String, String>();
            String shopname = div.getString("nick");
            String location = div.getString("item_loc");
            String money = div.getString("view_price");
            String name = div.getString("raw_title");
            String num = div.getString("view_sales");
            String detail_url = div.getString("detail_url");
            System.out.println(String.format("%s,%s,%s,%s,%s", shopname, location, money, name, num));
            item.put("shopname", shopname);
            item.put("location", location);
            item.put("money", money);
            item.put("name", name);
            item.put("num", num);
            item.put("detail_url",detail_url);
//            System.out.println(item);
            results.add(item);
        }
        return results;
    }
}

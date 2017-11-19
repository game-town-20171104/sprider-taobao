package com.ylfin.spider.utils;

public class SpiderUtils {

    /**
     * 取消jsonp包裹
     * @param jsonp
     * @return
     */
    public static String unwarpJSONP(String jsonp) {
        int length = jsonp.indexOf("(");
        String json = jsonp.substring(length + 1);
        int end = json.lastIndexOf(")");
        json = json.substring(0, end);
        return json;
    }

    public static String getNum(String viewNum) {
        return viewNum.replaceAll("[^0-9]", "");
    }

}

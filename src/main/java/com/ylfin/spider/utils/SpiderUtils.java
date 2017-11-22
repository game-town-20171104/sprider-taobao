package com.ylfin.spider.utils;

import java.util.Random;

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
    public static Integer randomInteger(Integer start, Integer max) {
        int distance = max - start + 1;
        Integer result = new Random().nextInt(distance) + start;
        return result;

    }
    public static String getNum(String viewNum) {
        return viewNum.replaceAll("[^0-9]", "");
    }

}

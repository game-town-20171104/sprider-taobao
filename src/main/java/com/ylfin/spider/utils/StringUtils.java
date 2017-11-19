package com.ylfin.spider.utils;

public class StringUtils {

    public static String replaceSpaceWithPlus(String keyword) {
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(keyword))
            return org.apache.commons.lang3.StringUtils.replace(keyword, " ", "+");
        return new String("");
    }


}

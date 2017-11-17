package com.ylfin.spider.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String format(){
        return  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String format(String  format){
        return  new SimpleDateFormat(format).format(new Date());
    }

    public static String format(Date date,String  format){
        return  new SimpleDateFormat(format).format(date);
    }

}

package com.ylfin.spider.vo;

import java.util.ArrayList;
import java.util.List;

public class KeywordsQueue {
    private List<String > keywords = new ArrayList<>();
    private int  current =0 ;

    public void addKeyword(String keyword){
        keywords.add(keyword);
    }


    public synchronized String getKeyword(){
        if(current==keywords.size()){
            return  null;
        }

        return keywords.get(current++);
    }

    public int getSize(){
        return keywords.size();
    }
}

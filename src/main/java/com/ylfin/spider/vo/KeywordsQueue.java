package com.ylfin.spider.vo;

import com.ylfin.spider.vo.bean.KeyWords;

import java.util.ArrayList;
import java.util.List;

public class KeywordsQueue {
    private List<KeyWords> keywords = new ArrayList<>();
    private int  current =0 ;

    public void addKeyword(KeyWords keyWords){
        keywords.add(keyWords);
    }


    public synchronized KeyWords getKeyword(){
        if(current==keywords.size()){
            return  null;
        }

        return keywords.get(current++);
    }

    public int getSize(){
        return keywords.size();
    }
}

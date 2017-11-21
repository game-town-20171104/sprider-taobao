package com.ylfin.spider.vo;

import com.ylfin.spider.vo.bean.KeyWords;

import java.util.ArrayList;
import java.util.List;

public class SpiderQueue<T> {
    private List<T> items = new ArrayList<>();
    private int  current =0 ;

    public void add(T item){
        items.add(item);
    }


    public synchronized T get(){
        if(current==items.size()){
            return  null;
        }

        return items.get(current++);
    }

    public int getSize(){
        return items.size();
    }
}

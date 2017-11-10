package com.ylfin.spider.vo.bean;

import java.io.Serializable;

public class KeyWords implements Serializable{
    private Long id ;
    private String title;

    public KeyWords(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "KeyWords{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}

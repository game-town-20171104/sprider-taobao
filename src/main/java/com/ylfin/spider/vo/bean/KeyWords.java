package com.ylfin.spider.vo.bean;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
@Data
@AllArgsConstructor
@TableName("keywords")
public class KeyWords implements Serializable{
    private Long id ;
    private String title;
    private Boolean active;


    public KeyWords(Long id, String title) {
        this.id = id;
        this.title = title;
    }


}

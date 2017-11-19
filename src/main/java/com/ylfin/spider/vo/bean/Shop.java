package com.ylfin.spider.vo.bean;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("shop")
public class Shop {
    private Long id ;
    private String name;
    private String url;
    private Boolean active;



}

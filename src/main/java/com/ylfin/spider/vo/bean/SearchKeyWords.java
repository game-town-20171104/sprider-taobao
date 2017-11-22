package com.ylfin.spider.vo.bean;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("search_keywords")
public class SearchKeyWords {
    private Long id;
    private String name ;
    private String shopCode ;
    private Boolean active;
}

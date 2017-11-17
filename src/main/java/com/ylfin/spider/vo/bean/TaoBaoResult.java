package com.ylfin.spider.vo.bean;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("taobao_result")
public class TaoBaoResult   {
    private Long id;
    private Long keywordsId;
    private int commentCount ;
    private String  spiderDate ;
    private String detailUrl;
    private Double price;
    private String shopName;
    private int num ;
    private String title ;
    private String location;


}

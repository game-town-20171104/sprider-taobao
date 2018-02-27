package com.ylfin.spider.eshop.vo.bean;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_eshop")
public class EshopBean {
    @TableId
    private Long id;
    private String game;
    private String country;
    private Double price;
    private Date spiderTime ;

}

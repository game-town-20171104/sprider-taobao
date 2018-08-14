package com.ylfin.spider.register.vo;

import lombok.Data;

/**
 * @author: godslhand
 * @date: 2018/8/14
 * @description:
 */
@Data
public class NintendoPwdVO {
    private String path ;
    private String account ;
    private String password;
    private String newPwd;
    private int rowIndex;
}

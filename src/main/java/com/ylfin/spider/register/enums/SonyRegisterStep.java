package com.ylfin.spider.register.enums;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum SonyRegisterStep {

    STEP_01(1,"注册信息填写"),STEP_02(2,"验证邮箱"),STEP_03(3,"修改个人信息"),STEP_04(4,"安全提示问题");

    private int code ;
    private String value;
}

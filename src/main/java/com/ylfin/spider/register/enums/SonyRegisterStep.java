package com.ylfin.spider.register.enums;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum SonyRegisterStep {

    STEP_01(1,"注册信息填写"),STEP_02(2,"验证邮箱"),STEP_03(3,"修改个人信息"),STEP_04(4,"安全提示问题"),
    STEP_05(5,"生成在线id"),STEP_06(6,"修改地址"),STEP_07(7,"修改账户信息"),STEP_08(8,"修改手机");

    private int code ;
    private String value;
}

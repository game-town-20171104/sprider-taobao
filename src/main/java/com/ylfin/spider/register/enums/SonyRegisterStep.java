package com.ylfin.spider.register.enums;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum SonyRegisterStep {

    STEP_100(100,"注册信息填写"), STEP_200(200,"验证邮箱"), STEP_300(300,"修改个人信息"), STEP_400(400,"安全提示问题"),
    STEP_210(210,"生成在线id"), STEP_500(500,"生成在线id（用户管理-过期）"), STEP_600(600,"修改地址"), STEP_700(700,"修改账户信息"), STEP_800(800,"修改手机");

    private int code ;
    private String value;
}

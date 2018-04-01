package com.ylfin.spider.register.enums;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public enum  RegisterType {

    mail163("网易"),sony("索尼"),catePrice("分类价格爬取"),nintendo("任天堂"),mailAoi("Aoi邮箱");

    private String name ;

}

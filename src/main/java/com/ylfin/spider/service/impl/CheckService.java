package com.ylfin.spider.service.impl;

import com.ylfin.spider.component.BaseSpider;
import com.ylfin.spider.component.checker.JavaMailChecker;
import com.ylfin.spider.component.checker.ProtonMailChecker;
import com.ylfin.spider.register.enums.RegisterType;
import com.ylfin.spider.register.vo.bean.MailBean;

public class CheckService {

    public static String getCheckCode(MailBean mailBean, RegisterType registerType, BaseSpider baseSpider) {

        if(mailBean==null||mailBean.getEmail()==null){
            throw new RuntimeException("邮箱不能为空");
        }
        String email = mailBean.getEmail();
        if(email.endsWith("gamesheaven.tech")){
            return new JavaMailChecker().getCheckCode(mailBean,registerType);
        }
        if(email.endsWith("protonmail.com")){
            return new ProtonMailChecker(baseSpider).getCheckCode(mailBean, registerType);
        }
        return null;
    }

}

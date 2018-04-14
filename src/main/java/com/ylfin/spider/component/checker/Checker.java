package com.ylfin.spider.component.checker;

import com.ylfin.spider.component.BaseSpider;
import com.ylfin.spider.register.enums.RegisterType;
import com.ylfin.spider.register.vo.bean.MailBean;

public abstract class Checker {

    private BaseSpider baseSpider;

    public Checker(BaseSpider baseSpider) {
        this.baseSpider = baseSpider;
    }

    public abstract String  getCheckCode(MailBean mailBean, RegisterType registerType);

    public  static Checker getChecker(MailBean mailBean){
//
//        if(mailBean.getEmail().endsWith("protonmail.com")){
//            return new ProtonMailChecker(b);
//        }

        return null;
    }
}

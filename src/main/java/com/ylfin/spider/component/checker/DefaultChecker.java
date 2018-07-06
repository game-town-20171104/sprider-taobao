package com.ylfin.spider.component.checker;

import com.ylfin.spider.component.BaseSpider;
import com.ylfin.spider.register.enums.RegisterType;
import com.ylfin.spider.register.vo.bean.MailBean;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public abstract class DefaultChecker implements  Checker{
    private BaseSpider baseSpider;

    public DefaultChecker(BaseSpider baseSpider) {
        this.baseSpider = baseSpider;
    }

    @Override
    public  String  getCheckCode(MailBean mailBean, RegisterType registerType){
        String code = null;
        switch (registerType) {
            case sony:
                code=  this.sonyCode(mailBean);
                break;
            case nintendo:
                code=  this.nintendoCode(mailBean);
                break;
            default:
                break;
        }
        return  code;

    }

    protected abstract String nintendoCode(MailBean mailBean);

    protected abstract String sonyCode(MailBean mailBean);

}



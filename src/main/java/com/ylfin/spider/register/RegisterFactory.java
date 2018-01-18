package com.ylfin.spider.register;


import com.ylfin.spider.cateprice.CatePriceSpider;
import com.ylfin.spider.cateprice.service.CatePriceService;
import com.ylfin.spider.register.enums.RegisterType;
import com.ylfin.spider.register.service.MailService;
import com.ylfin.spider.register.service.NintendoService;
import com.ylfin.spider.register.service.SonyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegisterFactory {
    @Autowired
    private MailService mailService;

    @Autowired
    private SonyService sonyService;

    @Autowired
    private CatePriceService catePriceService;

    @Autowired
    private NintendoService nintendoService;

    public Register getRegister(RegisterType registerType) {
        switch (registerType) {
            case sony:
                return buildSonyRegister();
            case mail163:
                return createMail163Register();
            case catePrice:
                return new CatePriceSpider(catePriceService);
            case nintendo:
                return buildNintendRegister();
            default:
                break;
        }

        return null;
    }

    private Register buildNintendRegister() {
        NintendoRegister nintendoRegister = new NintendoRegister(nintendoService);
        nintendoRegister.setMailService(mailService);
        return nintendoRegister;
    }


    private SonyRegister buildSonyRegister(){
        SonyRegister sonyRegister =  new SonyRegister(sonyService);
        sonyRegister.setMailService(mailService);
        return  sonyRegister;
    }


    private Mail163Register createMail163Register() {
        Mail163Register register = new Mail163Register(mailService);
        return register;
    }
}

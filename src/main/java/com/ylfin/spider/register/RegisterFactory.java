package com.ylfin.spider.register;


import com.ylfin.spider.cateprice.CatePriceSpider;
import com.ylfin.spider.cateprice.service.CatePriceService;
import com.ylfin.spider.eshop.service.EshopService;
import com.ylfin.spider.register.enums.RegisterType;
import com.ylfin.spider.register.service.MailService;
import com.ylfin.spider.register.service.NintendoService;
import com.ylfin.spider.register.service.SonyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private EshopService eshopService;

    @Value("${chrome-driver-path}")
    private String driverPath;

    public Register getRegister(RegisterType registerType) {
        switch (registerType) {
            case sony:
                return buildSonyRegister();
            case mail163:
                return createMail163Register();
            case catePrice:
                return buildCatePrice();
            case nintendo:
                return buildNintendRegister();
            case mailAoi:
                return buildAoiRegister();
            default:
                break;
        }

        return null;
    }

    private CatePriceSpider buildCatePrice() {
        return  new CatePriceSpider(catePriceService);
    }

    private Register buildNintendRegister() {
        NintendoRegister nintendoRegister = new NintendoRegister(nintendoService);
        nintendoRegister.setMailService(mailService);
        nintendoRegister.setBasePath(driverPath);
        return nintendoRegister;
    }


    private SonyRegister buildSonyRegister(){
        SonyRegister sonyRegister =  new SonyRegister(sonyService);
        sonyRegister.setMailService(mailService);
        sonyRegister.setBasePath(driverPath);
        return  sonyRegister;
    }

    private AoiRegister buildAoiRegister(){
        AoiRegister register = new AoiRegister(mailService);
        register.setBasePath(driverPath);
        return register;
    }

    private Mail163Register createMail163Register() {
        Mail163Register register = new Mail163Register(mailService);
        register.setBasePath(driverPath);
        return register;
    }
}

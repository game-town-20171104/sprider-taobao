package com.ylfin.spider.register;


import com.ylfin.spider.register.enums.RegisterType;
import com.ylfin.spider.register.service.MailService;
import com.ylfin.spider.register.service.SonyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegisterFactory {
    @Autowired
    private MailService mailService;

    @Autowired
    private SonyService sonyService;

    public Register getRegister(RegisterType registerType) {
        switch (registerType) {
            case sony:
                return new SonyRegister(sonyService);
            case mail163:
                return createMail163Register();
            default:
                break;
        }

        return null;
    }


    private Mail163Register createMail163Register() {
        Mail163Register register = new Mail163Register(mailService);
        return register;
    }
}

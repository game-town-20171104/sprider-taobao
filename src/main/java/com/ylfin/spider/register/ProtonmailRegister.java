package com.ylfin.spider.register;

import com.ylfin.spider.component.BaseSpider;
import com.ylfin.spider.register.service.MailService;
import com.ylfin.spider.register.vo.bean.AoiBean;
import com.ylfin.spider.register.vo.bean.MailBean;
import com.ylfin.spider.utils.PasswordUtils;
import com.ylfin.spider.utils.SpiderUtils;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

@Slf4j
public class ProtonmailRegister extends BaseSpider implements Register<MailBean> {

    private static final String recoveryMail="274072372@qq.com";
    private static final String lastName="sd";
    private static final String birthYear="1986";
    private static final String birthDay="16";
    private static final String birthMoth="2";
    private static final String female="Female";
    private static final String zipCode="89101";
    private static final String question="In which city did your parents meet?";
    private static final String questionAs="wenzhou";

    private MailService mailService;

    public ProtonmailRegister(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void initRegister() {
        this.setHeadless(false);
        this.init();
    }

    @Override
    public void handle(MailBean mailBean) {
        log.info("开始注册：{}",mailBean);
        String url = "https://mail.protonmail.com/create/new?language=en";
        String mail = mailBean.getEmail();

        if(mail==null||!mail.endsWith("@protonmail.com")){
            log.warn("{}：不是protonmail.com的邮箱，跳过");
            return;
        }
        this.getDriver().get(url);

        this.waitFindElementByName("username").sendKeys(mailBean.getUsername());
        String password = PasswordUtils.generatePassword();
        mailBean.setPassword(password);
        log.info(mailBean.getEmail()+" password is:"+password);
        this.waitFindElementByName("password").sendKeys(password);
        this.waitFindElementByName("passwordc").sendKeys(password);
        this.waitFindElementByName("notificationEmail").sendKeys(recoveryMail);
        this.waitFindElementByClass("signUpProcess-btn-create").click();
        this.waitFindElement(By.xpath("//*[@id=\"welcomeModal\"]"),3*60);
        mailBean.setSuccess(true);
        mailService.update(mailBean);
        log.info("注册完成");
    }


    @Override
    public void close() {
        this.quit();
    }
}

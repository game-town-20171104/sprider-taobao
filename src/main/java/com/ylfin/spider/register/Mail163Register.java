package com.ylfin.spider.register;

import com.ylfin.spider.component.BaseSpider;
import com.ylfin.spider.register.service.MailService;
import com.ylfin.spider.register.vo.bean.MailBean;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mail163Register extends BaseSpider implements Register<MailBean> {
    Logger logger = LoggerFactory.getLogger(getClass());

    String url ="http://reg.email.163.com/unireg/call.do?cmd=register.entrance&from=163navi&regPage=163";

    private MailService mailService;

    public Mail163Register(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void initRegister() {
        super.setHeadless(false);
        super.init();
    }

    @Override
    public void handle(MailBean mail) {
        logger.info("开始注册：{}",mail);

        String[]   source =mail.getEmail().split("@");
        String souceType =source[1];
        if(source.length<2){
            throw new RuntimeException(String.format("%s邮箱格式错误",mail));
        }
        if(!source.equals("163.com")&&!source.equals("126.com")){
            logger.warn("{}：不是网易邮箱，跳过",mail);
            return;
        }

        getDriver().get(url);
        this.waitFindElement(By.id("nameIpt")).sendKeys(mail.getUsername());


        Select select = new Select(this.waitFindElement(By.id("mainDomainSelect")));
        select.selectByValue(souceType);
        this.waitFindElement(By.id("mainPwdIpt")).sendKeys(mail.getPassword());
        this.waitFindElement(By.id("mainCfmPwdIpt")).sendKeys(mail.getPassword());
        this.waitFindElement(By.id("mainMobileIpt")).sendKeys(mail.getPhone());
        this.waiteTitleCondition("新用户注册成功");
        logger.info("注册成功：{}",mail);
        mailService.setSuccess(mail.getId());
    }

    @Override
    public void close() {
        super.quit();
    }

    public static void main(String[] args) {



    }

}

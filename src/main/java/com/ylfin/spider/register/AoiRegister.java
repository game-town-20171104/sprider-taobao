package com.ylfin.spider.register;

import com.ylfin.spider.component.BaseSpider;
import com.ylfin.spider.register.service.MailService;
import com.ylfin.spider.register.vo.bean.AoiBean;
import com.ylfin.spider.register.vo.bean.MailBean;
import com.ylfin.spider.utils.PasswordUtils;
import com.ylfin.spider.utils.SpiderUtils;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.ui.Select;

@Slf4j
public class AoiRegister extends BaseSpider implements Register<MailBean> {

    private static final String url="https://i.aol.com/reg/signup?locale=en_US";
    private static final String firstName="sd";
    private static final String lastName="sd";
    private static final String birthYear="1986";
    private static final String birthDay="16";
    private static final String birthMoth="2";
    private static final String female="Female";
    private static final String zipCode="89101";
    private static final String question="In which city did your parents meet?";
    private static final String questionAs="wenzhou";

    private MailService mailService;

    public AoiRegister(MailService mailService) {
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
        String url = "https://i.aol.com/reg/signup?locale=en_US";
        String mail = mailBean.getEmail();

        if(mail==null||!mail.endsWith("@aoi.com")){
            log.warn("{}：不是aol.com的邮箱，跳过");
            return;
        }
        this.getDriver().get(url);
        this.waitFindElementById("firstName").sendKeys(firstName);
        this.waitFindElementById("lastName").sendKeys(lastName);
        this.waitFindElementById("desiredSN").sendKeys(mailBean.getUsername());
        String password = PasswordUtils.generatePassword();
        mailBean.setPassword(password);
        this.waitFindElementById("password").sendKeys(password);
        this.waitFindElementById("verifyPassword").sendKeys(password);
        new Select(this.waitFindElementById("dobMonth")).selectByValue(birthMoth);
        this.waitFindElementById("dobDay").sendKeys(birthDay);
        this.waitFindElementById("dobYear").sendKeys(birthYear);
        new Select(this.waitFindElementById("gender")).selectByValue(female);
        this.waitFindElementById("zipCode").sendKeys(zipCode);
        new Select(this.waitFindElementById("acctSecurityQuestion")).selectByVisibleText(question);
        this.waitFindElementById("acctSecurityAnswer").sendKeys(questionAs);
        this.waitFindElementById("mobileNum").sendKeys(mailBean.getPhone());
//        this.waiteTitleCondition("AOL | Sign Up",60*1);
        this.waitFindElementByText("Congratulations",3*60);
        mailBean.setSuccess(true);
        mailService.update(mailBean);
        log.info("注册完成");
    }


    @Override
    public void close() {
        this.quit();
    }
}

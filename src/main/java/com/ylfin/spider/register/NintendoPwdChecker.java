package com.ylfin.spider.register;

import com.ylfin.spider.component.BaseSpider;
import com.ylfin.spider.register.service.NintendoService;
import com.ylfin.spider.register.service.NitendoPwdService;
import com.ylfin.spider.register.vo.NintendoPwdVO;
import com.ylfin.spider.register.vo.bean.NintendoBean;
import com.ylfin.spider.utils.PasswordUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * @author: godslhand
 * @date: 2018/8/13
 * @description:
 */
public class NintendoPwdChecker extends BaseSpider implements Register<NintendoPwdVO> {

    private NitendoPwdService nitendoPwdService;

    public NintendoPwdChecker(NitendoPwdService nitendoPwdService) {
        this.nitendoPwdService = nitendoPwdService;
    }

    @Override
    public void initRegister() {
        super.setHeadless(false);
        super.init();
    }

    @Override
    public void handle(NintendoPwdVO nintendoBean) {
        String url ="https://accounts.nintendo.com/login";
        String checkUrl ="https://www.nintendo.com/games/detail/skypeace-switch";
        String jpCheckUrl="https://ec.nintendo.com/JP/ja/titles/70010000009817";
        getDriver().get(url);
        this.waitFindElementByName("subject_id").sendKeys(nintendoBean.getAccount());
        this.waitFindElementByName("subject_password").sendKeys(nintendoBean.getPassword());
        this.waitFindElementById("accounts-login-button").click();
        //TODO check login
        this.waiteCondition(By.className("c-header_accountButton_nickname"),5*60);
        new Select(this.waitFindElementByClass("js-languageSelector")).selectByValue("en-US");
        if(isJapan()){
            this.openNewWindow(jpCheckUrl);
            this.switch2NewWindow();
            this.waitFindElementByClass("wide01").click();
        }else{
            this.openNewWindow(checkUrl);
            this.switch2NewWindow();
            try {
                this.waitFindElementByClass("btn-orange-filled").click();
            } catch (Exception e) {
                this.getDriver().navigate().refresh();
                this.waitFindElementByClass("btn-orange-filled").click();
            }

        }
        this.waitFindElementByName("subject_password").sendKeys(nintendoBean.getPassword());
        this.waitFindElementByClass("formInput-submit").click();
        //Check false
//        this.waiteCondition(By.className("error-code ng-binding"),1*60);
        System.out.println("currentUrl:"+this.getDriver().getCurrentUrl());
        this.waitFindElementByText("9001-0026",1*60);
        System.out.println("确认账户已解绑！");

        this.switch2Window(this.getInitWindow());
        this.waitFindElementByAttr("data-l10n","ACCOUNT_NAVIGATION_LOGIN_AND_SECURITY").click();
        this.waitFindElementByAttr("href","/password/edit?show_nav=1").click();
//        this.waitFindElementByName("subject_password").sendKeys(nintendoBean.getPassword());
//        this.waitFindElementByClass("formInput-submit").click();
        String password = PasswordUtils.generatePassword();
        System.out.println("生成新的密码"+password+" for "+nintendoBean);
        this.waitFindElementByName("password").sendKeys(password);
        this.waitFindElementByName("password_for_confirmation").sendKeys(password);
        this.waitFindElementByClass("formInput-submit").click();
        System.out.println("提交密码修改");
        nitendoPwdService.save2Excel(nintendoBean);
        this.waiteCondition(By.className("StateComplete_text"),1*60);
    }

    private boolean isJapan() {
        WebElement element = this.waitFindElementsByClass("SettingDetails_text").get(3).findElement(By.tagName("span"));
        String tag =element.getAttribute("data-l10n");
       if("COUNTRY_NAME_JP".equals(tag)){
           return true;
       }
        return false;
    }

    @Override
    public void close() {
        super.quit();
    }



}

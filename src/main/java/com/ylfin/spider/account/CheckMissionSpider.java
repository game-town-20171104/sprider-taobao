package com.ylfin.spider.account;

import com.ylfin.spider.account.service.CheckMissionService;
import com.ylfin.spider.account.vo.bean.CheckMission;
import com.ylfin.spider.component.BaseSpider;
import com.ylfin.spider.component.checker.JavaMailChecker;
import com.ylfin.spider.register.Register;
import com.ylfin.spider.register.enums.RegisterType;
import com.ylfin.spider.register.service.MailService;
import com.ylfin.spider.register.vo.bean.MailBean;
import com.ylfin.spider.service.impl.CheckService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author: godslhand
 * @date: 2019/2/11
 * @description:
 */
@Slf4j
public class CheckMissionSpider extends BaseSpider implements Register<CheckMission> {

    @Autowired
    MailService mailService;

    @Autowired
    CheckMissionService checkMissionService;

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public void setCheckMissionService(CheckMissionService checkMissionService) {
        this.checkMissionService = checkMissionService;
    }

    @Override
    public void initRegister() {
        super.setHeadless(false);
        super.init();
    }

    @Override
    public void handle(CheckMission checkMission) {
        try {
            resetPwd(checkMission);

            this.waitFindElementByAttr("data-l10n","ACCOUNT_NAVIGATION_SHOP").click();
            this.switch2NewWindow();
//      List<WebElement> elementList = this.waitFindElementsByClass("o_c-list-simplex__body");
//      elementList.get(elementList.size()-1).click();
            this.waitFindElementByAttr("ng-click","shop_menu_ctrl.showDeviceUnlink()").click();
            this.switch2NewWindow();

            this.waitFindElementByName("subject_password").sendKeys(checkMission.getNewPassword());
            this.waitFindElementByAttr("type", "submit").click();


            try {
               WebElement webElement= this.waitFindElementByClass("o_c-button01__shape");
               if(webElement.getAttribute("ng-click").equals("device_unlink_ctrl.onUnlinkButtonClicked()")){
                    brightHandle(checkMission);
               }else {
                   unBrightHandle(checkMission);
               }
    //            this.waitFindElementsByAttr("ng-click","device_unlink_ctrl.onUnlinkButtonClicked()");

            } catch (Exception e) {
                checkMission.setError(e.getMessage());
                unBrightHandle(checkMission);
            }
            checkMission.setError("成功");
            System.out.println(checkMission.getAccount()+"操作完成");
        } catch (Exception e) {
            log.error(checkMission.getAccount()+"处理失败",e);
            String error = StringUtils.substring(e.getMessage(),0,255);
            checkMission.setError(error);
            checkMission.setResult(0);
        }finally {
            checkMissionService.updateById(checkMission);
        }
    }

    private void brightHandle(CheckMission checkMission){
        checkMission.setBright(true);
        if(1 ==checkMission.getStrategy().intValue()){
            checkMission.setResult(0);
            backPwd(checkMission);
        }
        if(2 ==checkMission.getStrategy().intValue()){
            checkMission.setResult(1);
            checkMission.setActive(false);
        }
        if(3 ==checkMission.getStrategy().intValue()){
            checkMission.setResult(0);
        }

    }

    private void unBrightHandle(CheckMission checkMission){
        checkMission.setBright(false);
        if(1 ==checkMission.getStrategy().intValue()){
            checkMission.setResult(1);
            checkMission.setActive(false);
        }
        if(2 ==checkMission.getStrategy().intValue()){
             checkMission.setResult(0);
             backPwd(checkMission);
        }
        if(3 ==checkMission.getStrategy().intValue()){
            checkMission.setResult(1);
            checkMission.setActive(false);
            backPwd(checkMission);
        }
    }



    private void backPwd(CheckMission checkMission){
        try {
            String url ="https://accounts.nintendo.com/";
            this.getDriver().get(url);
            this.waitFindElementByAttr("data-l10n","ACCOUNT_NAVIGATION_LOGIN_AND_SECURITY").click();
            this.waitFindElementByAttr("href","/password/edit?show_nav=1").click();
            this.waitFindElementByName("subject_password").sendKeys(checkMission.getPassword());
            this.waitFindElementByAttr("type", "submit").click();
            this.waitFindElementByName("password").sendKeys(checkMission.getPassword());
            this.waitFindElementByName("password_for_confirmation").sendKeys(checkMission.getPassword());
            this.waitFindElementByAttr("type", "submit").click();
            System.out.println("提交密码修改");
            this.waiteCondition(By.className("StateComplete_text"),1*60);
        } catch (Exception e) {

            throw new RuntimeException("密码还原失败"+e.getMessage(),e);
        }
    }

    private void resetPwd(CheckMission checkMission){
        try {
            String loginUrl = "https://accounts.nintendo.com/login";
            getDriver().get(loginUrl);
            this.waitFindElementByAttr("data-l10n", "LOGIN_FORM_FORGOT_PASSWORD").click();
            this.waitFindElementById("address").sendKeys(checkMission.getAccount());
            this.waitFindElementByAttr("type", "submit").click();
            this.simpleWaite(2000L);
            MailBean mailBean = mailService.findByEmail(checkMission.getAccount());
            String url = CheckService.getCheckCode(mailBean, RegisterType.nintendoPwdModify, this);
            if(!url.startsWith("https://accounts.nintendo.com/password/reset/authenticate")){
                this.simpleWaite(2*1000L);
                url = CheckService.getCheckCode(mailBean, RegisterType.nintendoPwdModify, this);
            }

            this.getDriver().get(url);
            new Select(this.waitFindElementByName("birth_month")).selectByVisibleText("2");
            new Select(this.waitFindElementByName("birth_day")).selectByVisibleText("16");
            new Select(this.waitFindElementByName("birth_year")).selectByVisibleText("1985");
            this.waitFindElementByAttr("type", "submit").click();

            this.waitFindElementByName("password").sendKeys(checkMission.getNewPassword());
            this.waitFindElementByName("password_for_confirmation").sendKeys(checkMission.getNewPassword());
            this.waitFindElementByAttr("type", "submit").click();

            this.waitFindElementByClass("c-btn-primary").click();
        } catch (Exception e) {
            throw new RuntimeException("忘记密码操作失败"+e.getMessage(),e);
        }
    }


    @Override
    public void close() {
        this.quit();
    }


    public static void main(String[] args) {
        CheckMission checkMission = new CheckMission();
        checkMission.setAccount("sd00470@gamesheaven.tech");
        checkMission.setPassword("11111");
        CheckMissionSpider checkMissionSpider = new CheckMissionSpider();
        checkMissionSpider.initRegister();
        checkMissionSpider.handle(checkMission);
        checkMissionSpider.quit();

    }
}

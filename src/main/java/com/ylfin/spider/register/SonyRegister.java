package com.ylfin.spider.register;

import com.ylfin.spider.Exception.RegisterException;
import com.ylfin.spider.component.BaseSpider;
import com.ylfin.spider.register.enums.SonyRegisterStep;
import com.ylfin.spider.register.service.SonyService;
import com.ylfin.spider.register.vo.bean.SonyBean;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SonyRegister extends BaseSpider implements Register<SonyBean> {
    String url = "https://account.sonyentertainmentnetwork.com/reg/account/create-account!input.action?request_locale=zh_HK";
    private SonyService sonyService;
    //    String url ="https://id.sonyentertainmentnetwork.com/create_account/?#/create_account/wizard/account_info_page1?entry=%2Fcreate_account";
    private Logger logger = LoggerFactory.getLogger(getClass());

    public SonyRegister(SonyService sonyService) {
        this.sonyService = sonyService;
    }

    @Override
    public void initRegister() {
        super.setHeadless(false);
        super.init();
    }

    @Override
    public void handle(SonyBean sonyBean) {

        try {
            getDriver().get(url);
//        this.waitFindElementByClass("caption ").click();
            registerInfo(sonyBean);
            checkEmail(sonyBean);
            sureEmail(sonyBean);
            login(sonyBean);

            this.waiteTitleCondition("首页");
            this.waitFindElementById("navAccount").click();
            changeSecurityQuestion(sonyBean);
            changeIntroduction(sonyBean);
            sonyService.setSuccess(sonyBean.getId());
        } catch (RegisterException e) {
            SonyRegisterStep step = e.getSonyRegisterStep();
            sonyService.updateStep(sonyBean.getId(),step);
            throw e;
        }

    }

    private void sureEmail(SonyBean sonyBean) {
        if (sonyBean.getStep() > SonyRegisterStep.STEP_02.getCode()) {
            logger.info("跳过邮箱确认");
            return;
        }
        logger.info("确认邮箱验证……");
        this.switch2Window(getInitWindow());
        this.waitFindElementById("emailVerificationSaveBtn").click();
    }

    private void registerInfo(SonyBean sonyBean) {
        if(sonyBean.getStep()> SonyRegisterStep.STEP_01.getCode()){
            logger.info("跳过注册");
            return;
        }
        logger.info("开始注册……");

        try {
            List<WebElement> webElementList = this.waitFindElements(By.xpath("//section/input[@class]"));
            webElementList.get(0).sendKeys(sonyBean.getPsn());
            webElementList.get(1).click();
            webElementList.get(3).sendKeys(sonyBean.getPassword());
            webElementList.get(4).sendKeys(sonyBean.getPassword());
//        this.waitFindElementByClass("wrapper-fitparent").click();
            List<WebElement> selects = this.waitFindElements(By.tagName("select"));
            new Select(selects.get(0)).selectByValue("1987");
            new Select(selects.get(1)).selectByValue("12");
            new Select(selects.get(2)).selectByValue("8");
//        new Select(selects.get(0)).selectByVisibleText(sonyBean.getCountry());
            new Select(selects.get(4)).selectByVisibleText(sonyBean.getProvince());
            new Select(selects.get(5)).selectByVisibleText("简体中文");
//        new Select(selects.get(2)).selectByVisibleText("简体中文");
//        this.waitFindElementById("createAccountButton")).click();//不点击，让申请人点击
//        this.waitFindElementByClass("wrapper-fitparent").click();
            this.waiteTitleCondition("核实您的电子邮件地址", 10 * 60);
        } catch (Exception e) {
            throw new RegisterException(sonyBean+""+SonyRegisterStep.STEP_01,e, SonyRegisterStep.STEP_01);
        }
    }

    /**
     * 修改简介
     *
     * @param sonyBean
     */
    private void changeIntroduction(SonyBean sonyBean) {
        if(sonyBean.getStep()> SonyRegisterStep.STEP_03.getCode()){
            logger.info("跳过修改简介");
            return;
        }
        logger.info("开始修改简介……");
        try {
            this.waitFindElementById("leftColumn_profileCatLabel").click();
            this.waitFindElementById("editProfileSocialIdentityButton").click();
            this.waitFindElementById("socialFirstNameField").sendKeys("sword");
            this.waitFindElementById("socialLastNameField").sendKeys("dancer");
            this.waitFindElementById("editProfileIdentityButton").click();
        } catch (Exception e) {
            throw new RegisterException(sonyBean+""+SonyRegisterStep.STEP_03,e,SonyRegisterStep.STEP_03);
        }

        //socialFirstNameField

    }

    /**
     * 修改安全问题
     *
     * @param sonyBean
     */
    private void changeSecurityQuestion(SonyBean sonyBean) {
        if(sonyBean.getStep()> SonyRegisterStep.STEP_04.getCode()){
            logger.info("跳过修改安全问题");
            return;
        }
        logger.info("开始修改安全问题……");
        try {
            this.waitFindElementById("leftColumn_securitySettingsCatLabel").click();
            this.waitFindElementById("currentPasswordField").sendKeys(sonyBean.getPassword());
            new Select(this.waitFindElementById("securityQuestionListField")).selectByVisibleText(sonyBean.getQuestion());
            this.waitFindElementById("securityAnswerField").sendKeys(sonyBean.getQuesAnswer());
            this.waitFindElementById("changeSecurityQuestionButton").click();
        } catch (Exception e) {
            throw new RegisterException(sonyBean+""+SonyRegisterStep.STEP_04,e,SonyRegisterStep.STEP_04);
        }
    }

    /**
     * 登录
     *
     * @param sonyBean
     */
    private void login(SonyBean sonyBean) {
        Integer step =  sonyBean.getStep()==null?0:sonyBean.getStep();
        if(step<=SonyRegisterStep.STEP_01.getCode()){
            return;
        }
        logger.info("开始登录……");
        String url = "https://id.sonyentertainmentnetwork.com/signin/#/signin?entry=%2Fsignin";
        getDriver().get(url);
        List<WebElement> webElements = this.waitFindElements(By.xpath("//input[@class]"));
        webElements.get(0).sendKeys(sonyBean.getPsn());
        webElements.get(0).sendKeys(sonyBean.getPassword());
    }

    /**
     * 邮箱验证
     *
     * @param sonyBean
     */
    private void checkEmail(SonyBean sonyBean) {
        if(sonyBean.getStep()> SonyRegisterStep.STEP_02.getCode()){
            logger.info("跳过邮箱验证");
            return;
        }
        logger.info("开始邮箱验证……");

        try {
            this.openNewWindow("https://mail.163.com/");
            this.switch2NewWindow();
            this.waitFindElementByClass("dlemail").sendKeys(sonyBean.getPsn());
            this.waitFindElementByClass("dlpwd").sendKeys(sonyBean.getPassword());
            this.waitFindElementByClass("u-loginbtn").click();

            this.waitFindElementByAttr("title", "收件箱").click();
            this.waitFindElement(By.xpath("//span[text()='账户登记成功确认']")).click();

            List<WebElement> elements = this.waitFindElementsByClass("frame-main");
            for (WebElement element : elements) {
                if (element.isDisplayed()) {
                    WebElement frame = element.findElement(By.tagName("iframe"));
                    this.switch2NewWindow();
                    break;
                }
            }
            this.waitFindElement(By.linkText("立即验证")).click();
            this.switch2NewWindow();

        } catch (Exception e) {
            throw new RegisterException(sonyBean+""+SonyRegisterStep.STEP_02,e,SonyRegisterStep.STEP_02);
        }

    }


    @Override
    public void close() {
        super.quit();
    }

    public static void main(String[] args) {

    }
}

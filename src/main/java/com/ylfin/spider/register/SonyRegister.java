package com.ylfin.spider.register;

import com.ylfin.spider.Exception.RegisterException;
import com.ylfin.spider.component.BaseSpider;
import com.ylfin.spider.register.enums.SonyRegisterStep;
import com.ylfin.spider.register.service.MailService;
import com.ylfin.spider.register.service.SonyService;
import com.ylfin.spider.register.vo.bean.MailBean;
import com.ylfin.spider.register.vo.bean.SonyBean;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SonyRegister extends BaseSpider implements Register<SonyBean> {
    String url = "https://asia.playstation.com/chs-hk/account/#settings";
    private SonyService sonyService;
    //    String url ="https://id.sonyentertainmentnetwork.com/create_account/?#/create_account/wizard/account_info_page1?entry=%2Fcreate_account";
    private Logger logger = LoggerFactory.getLogger(getClass());
    private MailService mailService;

    public SonyRegister(SonyService sonyService) {
        this.sonyService = sonyService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void initRegister() {
        super.setHeadless(false);
        super.init();
    }

    /**
     * 每一步都有编号，别修改顺序！！！！！！！！
     *
     * @param sonyBean
     */
    @Override
    public void handle(SonyBean sonyBean) {
        if (sonyBean.getStep() == null) {
            sonyBean.setStep(0);
        }

        try {
            logout();
            registerInfo_01(sonyBean);
            login(sonyBean);
            checkEmail_02(sonyBean);
            sureEmail(sonyBean);

            changeIntroduction_03(sonyBean);
            changeSecurityQuestion_04(sonyBean);
            crateOnlineId_05(sonyBean);
            updateAddress_06(sonyBean);
            updateAccountInfo_07(sonyBean);
            updateMobile_08(sonyBean);
            sonyService.setSuccess(sonyBean.getId());
        } catch (RegisterException e) {
            SonyRegisterStep step = e.getSonyRegisterStep();
            sonyService.updateStep(sonyBean.getId(), step);
            throw e;
        }

    }

    /**
     * 启动前先登出帐号
     */
    private void logout() {
        logger.info("先登出帐号");
        getDriver().get("https://account.sonyentertainmentnetwork.com/external/auth/logout!sso.action");

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

    /**
     * 填写注册信息
     * STEP_01
     *
     * @param sonyBean
     */
    private void registerInfo_01(SonyBean sonyBean) {
        if (sonyBean.getStep() > SonyRegisterStep.STEP_01.getCode()) {
            logger.info("跳过注册");
            return;
        }
        logger.info("开始注册……");

        try {
            getDriver().get(url);
//            this.waitFindElementById("nav-7").click();
            this.waitFindElement(By.linkText("创建账号")).click();
            this.switch2NewWindow();
            List<WebElement> webElementList = this.waitFindElements(By.xpath("//section/input[@class]"));
            webElementList.get(0).sendKeys(sonyBean.getPsn());
//            webElementList.get(1).click();
//            webElementList.get(3).sendKeys(sonyBean.getPassword());
//            webElementList.get(4).sendKeys(sonyBean.getPassword());
//        this.waitFindElementByClass("wrapper-fitparent").click();
            List<WebElement> selects = this.waitFindElements(By.tagName("select"));
            new Select(selects.get(0)).selectByValue("1987");
            new Select(selects.get(1)).selectByValue("12");
            new Select(selects.get(2)).selectByValue("8");
            new Select(this.waitFindElementById("regInput_Country")).selectByVisibleText(sonyBean.getCountry());
            new Select(this.waitFindElementById("regInput_Language")).selectByVisibleText("简体中文");
            new Select(this.waitFindElementById("account_address_provinceField")).selectByVisibleText(sonyBean.getProvince());
            this.waitFindElementById("account_password").sendKeys(sonyBean.getPassword());
            this.waitFindElementById("confirmPasswordField").sendKeys(sonyBean.getPassword());
//        new Select(selects.get(2)).selectByVisibleText("简体中文");
//        this.waitFindElementById("createAccountButton")).click();//不点击，让申请人点击
//        this.waitFindElementByClass("wrapper-fitparent").click();
            this.waiteTitleCondition( 10 * 60,"核实您的电子邮件地址","登录");
            this.regLogin(sonyBean);
        this.closeOthers();
            sonyService.updateStep(sonyBean.getId(), SonyRegisterStep.STEP_02);
        } catch (Exception e) {
            throw new RegisterException(sonyBean + "" + SonyRegisterStep.STEP_01, e, SonyRegisterStep.STEP_01);
        }
    }

    /**
     * 修改简介-真实姓名
     * STEP_03
     *
     * @param sonyBean
     */
    private void changeIntroduction_03(SonyBean sonyBean) {
        if (sonyBean.getStep() > SonyRegisterStep.STEP_03.getCode()) {
            logger.info("跳过修改简介");
            return;
        }
        logger.info("开始修改简介……");
        try {
            getDriver().get("https://account.sonyentertainmentnetwork.com/liquid/cam/account/profile/edit-profile-social-identity!input.action");
            WebElement firstName = this.waitFindElementById("socialFirstNameField");
            firstName.clear();
            firstName.sendKeys(sonyBean.getFirstName());
            WebElement lastName = this.waitFindElementById("socialLastNameField");
            lastName.clear();
            lastName.sendKeys(sonyBean.getLastName());
            //判断是否有验证码
            if (this.isExist(By.id("recaptcha-accessible-status"))) {
                logger.info("简介有验证码……");
            } else {
                this.waitFindElementById("editProfileIdentityButton").click();
            }
            logger.info("简介修改完成");
            this.waiteTitleCondition("账号详情", 10 * 60);
            sonyService.updateStep(sonyBean.getId(), SonyRegisterStep.STEP_04);

        } catch (Exception e) {
            throw new RegisterException(sonyBean + "" + SonyRegisterStep.STEP_03, e, SonyRegisterStep.STEP_03);
        }

        //socialFirstNameField

    }

    /**
     * 生成在线id
     * STEP_05
     */
    private void crateOnlineId_05(SonyBean sonyBean) {
        if (sonyBean.getStep() > SonyRegisterStep.STEP_05.getCode()) {
            logger.info("跳过在线id");
            return;
        }
        logger.info("开始生成在线id……");
        try {
            String url = "https://account.sonyentertainmentnetwork.com/liquid/cam/account/profile/edit-profile-online-id!input.action";
            //https://id.sonyentertainmentnetwork.com/id/management/#/p/security
            getDriver().get(url);
            this.waitFindElementById("handle").sendKeys(sonyBean.getUsername());
            this.waitFindElementById("saveOnlineIdButton").click();

            this.waiteTitleCondition("账号详情", 10 * 60);
            sonyService.updateStep(sonyBean.getId(), SonyRegisterStep.STEP_06);
        } catch (Exception e) {
            throw new RegisterException(sonyBean + "" + SonyRegisterStep.STEP_05, e, SonyRegisterStep.STEP_05);
        }
    }

    /**
     * 修改帐号详情--有验证码
     * STEP_07
     *
     * @param sonyBean
     */
    private void updateAccountInfo_07(SonyBean sonyBean) {
        if (sonyBean.getStep() > SonyRegisterStep.STEP_07.getCode()) {
            logger.info("跳过修改帐号详情");
            return;
        }
        logger.info("开始修改帐号详情……");
        try {
            String url = "https://account.sonyentertainmentnetwork.com/liquid/cam/account/profile/edit-identity!input.action";
            getDriver().get(url);
            WebElement lastname = this.waitFindElementById("lastNameField");
            lastname.clear();
            lastname.sendKeys(sonyBean.getLastName());
            WebElement firstName = this.waitFindElementById("firstNameField");
            firstName.clear();
            firstName.sendKeys(sonyBean.getFirstName());
            logger.info("等待用戶填写验证码");
            //TODO check success
            this.waiteTitleCondition("账号详情", 10 * 60);
            logger.info("帐号详情修改成功！");
//            this.waitFindElementByClass("toutRow");
            //toutRow  您的身份信息已保存。
//            this.waitFindElement(By.xpath("//span[text()='您的身份信息已保存。']"));
            sonyService.updateStep(sonyBean.getId(), SonyRegisterStep.STEP_08);
        } catch (Exception e) {
            throw new RegisterException(sonyBean + "" + SonyRegisterStep.STEP_07, e, SonyRegisterStep.STEP_07);
        }

    }

    /**
     * 修改地址
     *
     * @param sonyBean STEP_06
     */
    private void updateAddress_06(SonyBean sonyBean) {
        if (sonyBean.getStep() > SonyRegisterStep.STEP_06.getCode()) {
            logger.info("跳过修改地址");
            return;
        }
        logger.info("开始修改地址……");
        try {
            String url = "https://account.sonyentertainmentnetwork.com/liquid/cam/account/profile/edit-location!input.action";
            getDriver().get(url);
            this.waitFindElementById("address_address1Field_input").sendKeys(sonyBean.getAddress());
            this.waitFindElementById("address_cityField_input").sendKeys(sonyBean.getCity());
            this.waitFindElementById("saveLocationButton").click();
            sonyService.updateStep(sonyBean.getId(), SonyRegisterStep.STEP_07);
        } catch (Exception e) {
            throw new RegisterException(sonyBean + "" + SonyRegisterStep.STEP_06, e, SonyRegisterStep.STEP_06);
        }
    }

    /**
     * 修改安全问题
     * STEP_04
     *
     * @param sonyBean
     */
    private void changeSecurityQuestion_04(SonyBean sonyBean) {
        if (sonyBean.getStep() > SonyRegisterStep.STEP_04.getCode()) {
            logger.info("跳过修改安全问题");
            return;
        }
        logger.info("开始修改安全问题……");
        try {
            getDriver().get("https://account.sonyentertainmentnetwork.com/liquid/cam/account/profile/edit-security-question!input.action");
            this.waitFindElementById("currentPasswordField").sendKeys(sonyBean.getPassword());
            new Select(this.waitFindElementById("securityQuestionListField")).selectByVisibleText(sonyBean.getQuestion());
            this.waitFindElementById("securityAnswerField").sendKeys(sonyBean.getQuesAnswer());
            this.waitFindElementById("changeSecurityQuestionButton").click();
            sonyService.updateStep(sonyBean.getId(), SonyRegisterStep.STEP_05);
        } catch (Exception e) {
            throw new RegisterException(sonyBean + "" + SonyRegisterStep.STEP_04, e, SonyRegisterStep.STEP_04);
        }
    }

    /**
     * 注册后需要登录的场景
     */
    private void regLogin(SonyBean sonyBean){
        try {
            WebElement userInput =this.waitFindElementByAttr("type","email");
            userInput.clear();
            userInput.sendKeys(sonyBean.getPsn());
            WebElement password =this.waitFindElementByAttr("type","password");
            password.clear();
            password.sendKeys(sonyBean.getPassword());
            this.waiteTitleCondition("核实您的电子邮件地址",10*60);
        } catch (Exception e) {
            logger.error("注册后登录失败！！！",e);
        }
    }


    /**
     * 登录
     * 注冊成功之后，后续流程异常，需要使用登录功能
     *
     * @param sonyBean
     */
    private void login(SonyBean sonyBean) {
        int i = 0;
        while (true) {
            try {
                i++;
                Integer step = sonyBean.getStep() == null ? 0 : sonyBean.getStep();
                if (step <= SonyRegisterStep.STEP_01.getCode()) {
                    return;
                }
                logger.info("开始登录……");
                String url = "https://asia.playstation.com/chs-hk/account/#settings";
                getDriver().get(url);
                this.waitFindElementById("signin-btn").click();
                this.waitFindElementById("signInInput_SignInID").sendKeys(sonyBean.getPsn());
                this.waitFindElementById("signInInput_Password").sendKeys(sonyBean.getPassword());
                this.waitFindElementById("signInButton").click();
                this.waitFindElementById("currentUserPC");
                break;
            } catch (Exception e) {
                logger.error("登录失败,重新登录", e);
                if (i > 2) {
                    throw new RuntimeException("登录失败" + i + "次，不再登录");
                }

            }
        }

    }

    /**
     * 邮箱验证
     * STEP_02
     *
     * @param sonyBean
     */
    private void checkEmail_02(SonyBean sonyBean) {
        if (sonyBean.getStep() > SonyRegisterStep.STEP_02.getCode()) {
            logger.info("跳过邮箱验证");
            return;
        }
        logger.info("开始邮箱验证……");
        MailBean mailBean = mailService.findByEmail(sonyBean.getPsn());
        String password;
        if (mailBean == null) {

            throw new RegisterException(sonyBean + " 未找到对应的邮箱密码", SonyRegisterStep.STEP_02);
        }
        password = mailBean.getPassword();

        try {
            String verifyUrl = "https://account.sonyentertainmentnetwork.com/security/unverified-user!input.action";
            if(!getDriver().getCurrentUrl().startsWith(verifyUrl)){
                getDriver().get(verifyUrl);
                //点击重新发送
                this.waitFindElementById("sendEmailButton").click();
            }


            this.openNewWindow("https://mail.163.com/");
            this.switch2NewWindow();
            this.switchFrame(this.waitFindElementById("x-URS-iframe"));
            this.waitFindElementByClass("dlemail").sendKeys(sonyBean.getPsn());
            this.waitFindElementByClass("dlpwd").sendKeys(password);
            this.waitFindElementByClass("u-loginbtn").click();

            this.waitFindElementByAttr("title", "收件箱").click();
            this.waitFindElement(By.xpath("//*[text()='账户登记成功确认']")).click();

            List<WebElement> elements = this.waitFindElementsByAttr("aria-label", "读信");
            for (WebElement element : elements) {
                if (element.isDisplayed()) {
                    WebElement frame = element.findElement(By.tagName("iframe"));
                    this.switchFrame(frame);
                    break;
                }
            }
            this.waitFindElement(By.linkText("立即验证")).click();
            this.switch2NewWindow();


        } catch (Exception e) {
            throw new RegisterException(sonyBean + "" + SonyRegisterStep.STEP_02, e, SonyRegisterStep.STEP_02);
        }

    }


    /**
     * 修改手机号
     *
     * @param sonyBean
     */
    private void updateMobile_08(SonyBean sonyBean) {
        String url = "https://id.sonyentertainmentnetwork.com/id/management/#/p/security";
        if (sonyBean.getStep() > SonyRegisterStep.STEP_08.getCode()) {
            logger.info("跳过手机修改");
            return;
        }
        logger.info("开始手机修改……");

        try {
            getDriver().get(url);

            this.waitFindElementsByClass("secondary-button").get(2).click();
            //添加手机
            this.waitFindElement(By.xpath("//span[text()='添加手机']")).click();
            this.waitFindElementByClass("selected-flag").click();
            this.waitFindElementByAttr("data-country-code", "cn").click();
            this.waitFindElementByClass("theme-textfield").sendKeys(sonyBean.getPhone());
            this.waitFindElementByClass("primary-button").click();
            logger.info("等待用戶輸入短信……");
            this.waitFindElementByClass("icon-succeed");
        } catch (Exception e) {
            throw new RegisterException(sonyBean + "" + SonyRegisterStep.STEP_08, e, SonyRegisterStep.STEP_08);
        }


    }


    @Override
    public void close() {
        super.quit();
    }

    public static void main(String[] args) {

    }
}

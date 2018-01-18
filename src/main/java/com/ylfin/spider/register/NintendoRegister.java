package com.ylfin.spider.register;

import com.ylfin.spider.component.BaseSpider;
import com.ylfin.spider.component.MailHelper;
import com.ylfin.spider.register.service.MailService;
import com.ylfin.spider.register.service.NintendoService;
import com.ylfin.spider.register.vo.bean.MailBean;
import com.ylfin.spider.register.vo.bean.NintendoBean;
import com.ylfin.spider.utils.SpiderUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class NintendoRegister extends BaseSpider implements Register<NintendoBean> {

    private NintendoService nitendoService;

    private MailService mailService;

    public NintendoRegister(NintendoService nitendoService) {
        this.nitendoService = nitendoService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void initRegister() {
        super.setHeadless(false);
        super.init();
    }

    @Override
    public void handle(NintendoBean nintendoBean) {
        nintendoBean.setPassword(generatePassword());
        String url ="https://accounts.nintendo.com/login";
        getDriver().get(url);
        this.scrollSlow2end();
        this.waitFindElementByClass("btn-primaryInverse").click();
        this.waitFindElementsByClass("AuthorizeAgeGate_box_item_image").get(1).click();
        this.waitFindElementByAttr("name","nickname").sendKeys(nintendoBean.getNickname());
        this.waitFindElementById("email").sendKeys(nintendoBean.getEmail());
        this.simpleRandomWaite(1);
        this.waitFindElementByAttr("name","subject_password").sendKeys(nintendoBean.getPassword());
        this.waitFindElementByAttr("name","subject_password_for_confirmation").sendKeys(nintendoBean.getPassword());
        //1985.02.16
        this.simpleRandomWaite(1);
        new Select(this.waitFindElementById("birth-year-field")).selectByValue("1985");
        new Select(this.waitFindElementById("birth-month-field")).selectByVisibleText("2");
        new Select(this.waitFindElementById("birth-day-field")).selectByValue("16");
        //性别
        this.simpleRandomWaite(1);
        new Select(this.waitFindElementByAttr("name","gender")).selectByValue("male");
        new Select(this.waitFindElementById("country-field")).selectByVisibleText("日本");

         this.waitFindElementById("form-terms_consented").click();
        this.waitFindElementByClass("formInput-submit").click();
         String code =this.getCheckCode(nintendoBean);
        WebElement codeElement = this.waitFindElementByClass("formInput-large");
        codeElement.clear();
        codeElement.sendKeys(code);
        this.waitFindElementByClass("formInput-submit").click();
        nintendoBean.setSuccess(true);
        nitendoService.update(nintendoBean);

    }

    /**
     * 7個字母一個數字
     * @return
     */
    private  String generatePassword(){
        String[] seed1={"A","B","C","D","E","F","G","H","J","K","M","N","Q","R","S","T","U","V","W","X","Y","Z"};
        String[] seed2 = {"1","2","3","4","5","6","7","8","9"};

        StringBuilder sb = new StringBuilder();
        boolean hasNum =false;
        for(int i=0;i<8;i++){

            if(hasNum){
                sb.append(seed1[SpiderUtils.randomInteger(0,seed1.length-1)]);
                continue;
            }
            if (!hasNum&&i==7){
                sb.append(seed2[SpiderUtils.randomInteger(0,seed2.length-1)]);
                continue;
            }

            int randomNum =SpiderUtils.randomInteger(0,seed1.length+seed2.length-1);
            if(randomNum>=seed1.length){
                hasNum =true;
                sb.append(seed2[randomNum-seed1.length]);
            }else {
                sb.append(seed1[randomNum]);
            }

        }

        return sb.toString();
    }

    private String getCheckCode(NintendoBean nintendoBean) {
        //TODO find password
        this.simpleRandomWaite(1000,2000);
      MailBean mailBean= mailService.findByEmail(nintendoBean.getEmail());
        MailHelper mailHelper = new MailHelper(this);
        String code =mailHelper.getCheckCode(mailBean.getUsername(),mailBean.getPassword(),"Nintendo Account","");
        return code;
    }


    @Override
    public void close() {
        super.quit();
    }


    public static void main(String[] args) {
//        for (int i=0;i<100;i++){
//            System.out.println( generatePassword());
//        }
    }
}



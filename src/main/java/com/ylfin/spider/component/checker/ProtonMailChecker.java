package com.ylfin.spider.component.checker;

import com.ylfin.spider.component.BaseSpider;
import com.ylfin.spider.register.enums.RegisterType;
import com.ylfin.spider.register.vo.bean.MailBean;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ProtonMailChecker implements Checker {

    private static final String checkPath = "";
    private static final String loginPath = "https://mail.protonmail.com/login";
    private BaseSpider spider;

    public ProtonMailChecker(BaseSpider baseSpider) {
        this.spider =baseSpider;
    }

    @Override
    public String getCheckCode(MailBean mailBean, RegisterType registerType) {
        spider.openNewWindow(loginPath);
        spider.switch2NewWindow();
        String username = mailBean.getUsername();
        String password = mailBean.getPassword();
        spider.waitFindElementById("username").sendKeys(username);
        spider.waitFindElementById("password").sendKeys(password);
        spider.waitFindElementById("login_btn").click();
       return check(mailBean, registerType);
    }

    private String check(MailBean mailBean, RegisterType registerType) {
        String code ="";
        switch (registerType) {
            case sony:
                spider.simpleRandomWaite();
                spider.waitFindElement(By.xpath("//*[text()='账户登记成功确认']")).click();
                spider.waitFindElement(By.linkText("立即验证")).click();
                break;
            case nintendo:
                WebElement titleElement = spider.waitFindElement(By.xpath("//*[contains(text(),'Nintendo Account')]"));
                String str = titleElement.getText().split("]|】")[0];
                 code = str.substring(1);
                break;
            default:
                break;

        }
        spider.switch2Window(spider.getInitWindow());
        return code;
    }
}

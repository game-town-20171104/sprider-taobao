package com.ylfin.spider.component;

import com.ylfin.spider.Exception.RegisterException;
import com.ylfin.spider.register.enums.SonyRegisterStep;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

@Slf4j
public class MailHelper {

    private BaseSpider baseSpider;

    public MailHelper(BaseSpider baseSpider) {
        this.baseSpider = baseSpider;
    }

    public String getCheckCode(String username, String password, String title, String className) {
        try {

            baseSpider.openNewWindow("https://mail.163.com/");
            baseSpider.switch2NewWindow();
            baseSpider.switchFrame(baseSpider.waitFindElementById("x-URS-iframe"));
            baseSpider.waitFindElementByClass("dlemail").sendKeys(username);
            baseSpider.waitFindElementByClass("dlpwd").sendKeys(password);
            baseSpider.waitFindElementByClass("u-loginbtn").click();

            baseSpider.waitFindElementByAttr("title", "收件箱").click();
//            baseSpider.waitFindElement(By.xpath("//*[text()='" + title + "']")).click();
//            Nintendo Account: E-mail address verification
            WebElement titleElement = baseSpider.waitFindElement(By.xpath("//*[contains(text(),'" + title + "')]"));
            String str = titleElement.getText().split("]|】")[0];
            String code = str.substring(1);
//            List<WebElement> elements = baseSpider.waitFindElementsByAttr("aria-label", "读信");
//            for (WebElement element : elements) {
//                if (element.isDisplayed()) {
//                    WebElement frame = element.findElement(By.tagName("iframe"));
//                    baseSpider.switchFrame(frame);
//                    break;
//                }
//            }
//            String text =baseSpider.waitFindElementByClass(className).getText();
            baseSpider.switch2Window(baseSpider.getInitWindow());
            return code;

        } catch (Exception e) {
            log.error("邮箱验证码获取失败", e);
            throw e;
        }
    }
}

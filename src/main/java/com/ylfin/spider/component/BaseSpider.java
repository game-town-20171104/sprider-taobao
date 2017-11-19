package com.ylfin.spider.component;

import com.ylfin.spider.enums.OS;
import com.ylfin.spider.utils.SpiderUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class BaseSpider {
    WebDriver driver;

    public void init() {
        System.setProperty("webdriver.chrome.driver", getDriverPath());
        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--window-size=1920,1080");
         driver = (WebDriver) new ChromeDriver(chromeOptions);
    }


    public void quit(){
       if(driver!=null){
           driver.quit();
       }
    }

    public void simpleWaite() {
        this.simpleWaite(2*1000L);
    }

    public void simpleWaite(Long time ){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public  void waiteTitleCondition(String  keyword){
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith(keyword);
            }
        });
    }
    public  void waitBy(By by){
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(by));
    }
    public  void waitAndClick(WebElement element){
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }
    public  WebElement waitFindElement(By by){
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(by));
        return driver.findElement(by);
    }

    /**
     * xpath 方式class 获取元素 单个
     * @param className
     * @return
     */
    public WebElement waitFindElementByClass(String className){
        By by =By.xpath("//*[contains(@class,'"+className+"')]");
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(by));
        return driver.findElement(by);
    }
    /**
     * xpath 方式class 获取元素 批量
     * @param className
     * @return
     */
    public List<WebElement> waitFindElementsByClass(String className){
        By by =By.xpath("//*[contains(@class,'"+className+"')]");
        (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(by));
        return driver.findElements(by);
    }

    private String getDriverPath() {
        String basePath =this.getClass().getClassLoader().getResource("driver").getPath();
        switch (OS.getPlatform()){
            case MAC:
                basePath = basePath+"/chromedriver_mac32/chromedriver";
                break;
            case LINUX:
                basePath = basePath+"/chromedriver_linux64/chromedriver";
                break;
            case WINDOWS:
                basePath = basePath+"\\chromedriver_win32\\chromedriver.exe";
                break;
        }
        return   basePath;
    }
    public String loadPage(String url,Long sleep) {
        driver.get(url);
        if(sleep>0)
            simpleWaite(1000L);
        String content = this.waitFindElement(By.tagName("pre")).getText();
        return SpiderUtils.unwarpJSONP(content);
    }

}

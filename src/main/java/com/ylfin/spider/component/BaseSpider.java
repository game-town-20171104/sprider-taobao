package com.ylfin.spider.component;

import com.ylfin.spider.enums.OS;
import com.ylfin.spider.utils.SpiderUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class BaseSpider {
    Logger logger = LoggerFactory.getLogger(getClass());
    WebDriver driver;
    private int waitTime = 1*3;
    private String deviceName;
    private Proxy proxy;
    private boolean headless = true;
    private String initWindow;

    private String basePath;
    //    https://langyuedianzi.taobao.com/i/asyncSearch.htm?callback=jsonp148&pageNo=1&search=y&orderType=hotsell_desc
    public void init() {
        try {
            System.setProperty("webdriver.chrome.driver", getDriverPath());
            ChromeOptions chromeOptions = new ChromeOptions();
            if (headless) {
                chromeOptions.addArguments("--headless");
            }

            chromeOptions.addArguments("--window-size=1920,1080");
            System.out.println("chrome setting is ok ……");
            driver = new ChromeDriver(chromeOptions);
            System.out.println("chrome has started ......");
            this.initWindow = driver.getWindowHandle();
        } catch (Exception e) {
            throw new RuntimeException("chrome初始化失败：",e);
        }
    }

    /**
     * 读取profile的初始化
     */
    public void initWithProfile() {
        System.setProperty("webdriver.chrome.driver", getDriverPath());
        ChromeOptions chromeOptions = new ChromeOptions();
        if (headless) {
            chromeOptions.addArguments("--headless");
        }
        //设置用户配置文件夹
        String fileSplit = System.getProperty("file.separator");
//        String path = System.getProperty("user.home") + fileSplit + "AppData" + fileSplit + "Local" + fileSplit + "Google" + fileSplit + "Chrome" + fileSplit + "mobile";
//        chromeOptions.addArguments("--user-data-dir=" + path);
        chromeOptions.addArguments("--window-size=1920,1080");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();

        if (deviceName != null) {
            Map<String, String> mobileEmulation = new HashMap<>();
            mobileEmulation.put("deviceName", deviceName);
            chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
        }
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        if (this.proxy != null) {
            capabilities.setCapability("proxy", proxy);
        }
        driver = (WebDriver) new ChromeDriver(capabilities);
        this.initWindow = driver.getWindowHandle();
    }


    public void quit() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void simpleWaite() {
        this.simpleWaite(2 * 1000L);
    }

    public void simpleWaite(Long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendUnFocusAbleKey(WebElement element, CharSequence... keysToSend) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.click();
        actions.sendKeys(keysToSend);
        actions.build().perform();
    }

    /**
     * 等待title 出现关键词
     *
     * @param keyword
     */
    public void waiteTitleCondition(String keyword) {
        (new WebDriverWait(driver, waitTime)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith(keyword);
            }
        });
    }

    /**
     * 自定义超长等待
     * @param by
     * @param waitSeconds
     */
    public void waiteCondition(By by,int waitSeconds){
        (new WebDriverWait(driver, waitSeconds)).until(ExpectedConditions.elementToBeClickable(by));
    }
    /**
     * 自定义超长等待
     *
     * @param keyword
     * @param waitSeconds
     */
    public void waiteTitleCondition(String keyword, int waitSeconds) {
        (new WebDriverWait(driver, waitSeconds)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith(keyword);
            }
        });
    }


    public void waiteTitleCondition(int waitSeconds,String ... keyword ) {
        (new WebDriverWait(driver, waitSeconds)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                boolean result = false;
               String title = d.getTitle().toLowerCase();
                for (String word :keyword){
                    if(title.startsWith(word))
                        return true;
                }
                return result;
            }
        });
    }

    /**
     * 等待袁术
     *
     * @param by
     */
    public void waitBy(By by) {
        (new WebDriverWait(driver, waitTime)).until(ExpectedConditions.elementToBeClickable(by));
    }

    /**
     * 根据属性xpath查找
     *
     * @param attr
     * @param value
     * @return
     */
    public WebElement waitFindElementByAttr(String attr, String value) {
        By by = By.xpath("//*[contains(@" + attr + ",'" + value + "')]");
        (new WebDriverWait(driver, waitTime)).until(ExpectedConditions.elementToBeClickable(by));
        return driver.findElement(by);
    }
    public List<WebElement> waitFindElementsByAttr(String attr, String value) {
        By by = By.xpath("//*[contains(@" + attr + ",'" + value + "')]");
        (new WebDriverWait(driver, waitTime)).until(ExpectedConditions.elementToBeClickable(by));
        return driver.findElements(by);
    }

    public WebElement waitFindElementByText(String text){
        By by = By.xpath("//*[contains(text()," + text + "')]");
        (new WebDriverWait(driver, waitTime)).until(ExpectedConditions.elementToBeClickable(by));
        return driver.findElement(by);
    }
    public WebElement waitFindElementByText(String text,int waitSecond){
        By by = By.xpath("//*[contains(text()," + text + "')]");
        (new WebDriverWait(driver, waitSecond)).until(ExpectedConditions.elementToBeClickable(by));
        return driver.findElement(by);
    }

    /**
     * 等待元素并点击 ==》可用waitFindElementByClass 替换
     *
     * @param element
     */
    public void waitAndClick(WebElement element) {
        (new WebDriverWait(driver, waitTime)).until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    public WebElement waitFindElement(By by) {
        (new WebDriverWait(driver, waitTime)).until(ExpectedConditions.elementToBeClickable(by));
        return driver.findElement(by);
    }

    public List<WebElement> waitFindElements(By by) {
        (new WebDriverWait(driver, waitTime)).until(ExpectedConditions.elementToBeClickable(by));
        return driver.findElements(by);
    }

    public WebElement waitFindElementById(String id) {
        return this.waitFindElement(By.id(id));
    }

    /**
     * 判断是否存在用 1s
     * @param by
     * @return
     */
    public boolean isExist(By by){
        try {
            (new WebDriverWait(driver, 1)).until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            logger.info("判断元素是否存在："+e.getMessage());
            return false;
        }
        return  true;
    }

    /**
     * 新窗口中打开
     *
     * @param url
     */
    public void openNewWindow(String url) {
        String js = "window.open(\""+url+"\");";
        ((JavascriptExecutor) driver).executeScript(js);

    }

    /**
     * xpath 方式class 获取元素 单个
     *
     * @param className
     * @return
     */
    public WebElement waitFindElementByClass(String className) {
        By by = By.xpath("//*[contains(@class,'" + className + "')]");
        (new WebDriverWait(driver, waitTime)).until(ExpectedConditions.elementToBeClickable(by));
        return driver.findElement(by);
    }

    /**
     * xpath 方式class 获取元素 批量
     *
     * @param className
     * @return
     */
    public List<WebElement> waitFindElementsByClass(String className) {
        By by = By.xpath("//*[contains(@class,'" + className + "')]");
        (new WebDriverWait(driver, waitTime)).until(ExpectedConditions.elementToBeClickable(by));
        return driver.findElements(by);
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    /**
     * 获取不同环境下的chromedriver驱动路径
     *
     * @return
     */
    private String getDriverPath() {
        if(basePath ==null){
            basePath = this.getClass().getClassLoader().getResource("driver").getPath();
        }else if(basePath.startsWith("classpath:")){
            basePath = this.getClass().getClassLoader().getResource("").getPath()+basePath.substring(10);
        }
        System.out.println("路径:====>"+basePath);
//        String basePath ="D:/driver";
        switch (OS.getPlatform()) {
            case MAC:
                basePath = basePath + "/chromedriver_mac32/chromedriver";
                break;
            case LINUX:
                basePath = basePath + "\\chromedriver_linux64\\chromedriver";
                break;
            case WINDOWS:
                basePath = basePath + "\\chromedriver_win32\\chromedriver.exe";
                break;
        }
        return basePath;
    }

    public String loadPage(String url, Long sleep) {
        driver.get(url);
        if (sleep > 0)
            simpleWaite(1000L);
        String content = this.waitFindElement(By.tagName("pre")).getText();
        return SpiderUtils.unwarpJSONP(content);
    }

    /**
     *超时关闭加载
     * @param seconds
     * @param url
     */
    public void getOverdue(int seconds,String url){
        try{
            //设置超时时间为3S
            driver.manage().timeouts().pageLoadTimeout(seconds, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(seconds, TimeUnit.SECONDS);
            driver.get(url);
        }catch(Exception e){
            logger.warn("页面超时加载："+seconds+" 秒",e);
        }
    }

    private int randomInt(int position) {
        return new Random().nextInt(position);
    }

    private Long randomTime() {
        return (randomInt(5) + 1) * 100L;
    }

    /**
     * 0-n之间的秒等待
     *
     * @param seconds
     */
    public void simpleRandomWaite(int seconds) {
        simpleWaite(randomInt(seconds) * 1000L);
    }

    /**
     * 500毫秒-2秒之前的随机
     */
    public void simpleRandomWaite() {
        simpleWaite(SpiderUtils.randomInteger(500, 2000) * 1L);
    }

    /**
     * 毫秒之间的随机等待
     *
     * @param minMillisecond
     * @param maxMillisecond
     */
    public void simpleRandomWaite(int minMillisecond, int maxMillisecond) {
        simpleWaite(Long.valueOf(SpiderUtils.randomInteger(minMillisecond, maxMillisecond)));
    }

    /**
     * 逐步滚动到下面
     */
    public void scrollSlow2end() {
        simpleWaite(randomTime());
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight/3)");
        simpleWaite(randomTime());
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, (document.body.scrollHeight)*2/3)");
        simpleWaite(randomTime());
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        simpleWaite(randomTime());


    }

    /**
     * 逐步滚动到上面
     */
    public void scrollBack2Top() {
        simpleWaite(randomTime());
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, (document.body.scrollHeight)*2/3)");
        simpleWaite(randomTime());
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight/3)");
        simpleWaite(randomTime());
        ((JavascriptExecutor) driver).executeScript("window.scrollTo( 0,0)");
    }

    public WebDriver getDriver() {
        if (driver == null) {
            logger.warn("driver 为空，请先执行init 方法");
        }
        return driver;
    }

    /**
     * 关闭多余标签
     */
    public void closeOthers() {
        String curt = driver.getWindowHandle();
        Set<String> windows = driver.getWindowHandles();
        for (String handle : windows) {
            if (!curt.equals(handle)) {
                driver.switchTo().window(handle).close();
            }
        }
        driver.switchTo().window(curt);
        this.initWindow = curt;

    }

    public void switch2NewWindow() {
        String curt = driver.getWindowHandle();
        Set<String> windows = driver.getWindowHandles();
        System.out.println("oldTitle" + driver.getTitle());
        driver.switchTo().window(windows.toArray(new String[]{})[windows.size() - 1]);

    }

    public void switch2Window(String window) {
        driver.switchTo().window(window);
    }

    /**
     * 切换iframe
     *
     * @param frame
     */
    public void switchFrame(WebElement frame) {
        driver.switchTo().frame(frame);
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public void setHeadless(boolean headless) {
        this.headless = headless;
    }

    public String getInitWindow() {
        return initWindow;
    }
}

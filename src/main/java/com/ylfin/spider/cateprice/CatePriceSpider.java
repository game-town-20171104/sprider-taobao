package com.ylfin.spider.cateprice;

import com.ylfin.spider.cateprice.service.CatePriceService;
import com.ylfin.spider.cateprice.vo.bean.CatePrice;
import com.ylfin.spider.component.BaseSpider;
import com.ylfin.spider.component.TaoBaoHelper;
import com.ylfin.spider.register.Register;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

public class CatePriceSpider extends BaseSpider implements Register<CatePrice> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private TaoBaoHelper taoBaoHelper;

    private CatePriceService catePriceService;
    public CatePriceSpider(CatePriceService catePriceService) {
        this.catePriceService =catePriceService;
        taoBaoHelper = new TaoBaoHelper(this);
    }

    @Override
    public void initRegister() {
        super.setHeadless(false);
        super.init();
    }

    @Override
    public void handle(CatePrice catePrice) {
        taoBaoHelper.search(catePrice.getKeyword());
        int pageSize =3;
        int current =0;
        while (true){
            if(current>=pageSize){
                break;
            }
            WebElement parentItem =this.waitFindElementById("mainsrp-itemlist");
            List<WebElement> itemList =parentItem.findElements(By.xpath(".//*[contains(@class,'shopname') and child::span[contains(text(),'"+catePrice.getShop()+"')]]"));
            if(CollectionUtils.isEmpty(itemList)){
                //nextPage ;
                this.waitFindElementByAttr("title","下一页").click();
                current++;
                continue;
            }
            System.out.println("同家店铺找到商品数量："+itemList.size());
            for (WebElement element:itemList){
                WebElement cparent =element.findElement(By.xpath("./ancestor::*[@data-category]"));
                WebElement href = cparent.findElement(By.xpath(".//*[contains(@id,'J_Itemlist_TLink')]"));
                catePrice.setTitle(href.getText());
                System.out.println(href.getAttribute("href"));
                catePrice.setItemUrl(href.getAttribute("href"));
                href.click();
                this.switch2NewWindow();

                this.setAuthPrice(catePrice);
                this.setUnAuthPrice(catePrice);
                this.setAuthHalfPrice(catePrice);
                this.setOfflinePrice(catePrice);
                break;
            }
            catePrice.setSuccess(true);
            catePrice.setUpdateTime(new Date());
            catePriceService.update(catePrice);
            break;
        }


        System.out.println("========");

    }

    @Override
    public void close() {
        super.quit();
    }



    private void setAuthPrice(CatePrice catePrice){
        if(catePrice.getAuthName()==null)
            return;
        try {
            WebElement clickItem = this.waitFindElementByAttr("data-property","游戏版本").findElement(By.xpath(".//span[contains(text(),'"+catePrice.getAuthName()+"')]"));
            clickItem.click();
//            this.waitFindElementByAttr("data-property","语种分类").findElement(By.xpath(".//span[contains(text(),'简体中文')"));
            String price =this.waitFindElementByClass("tb-rmb-num").getText();
            catePrice.setAuthPrice(Double.valueOf(price));
        } catch (Exception e) {
            logger.info("未爬取到认证版");
        }
    }

    private void setAuthHalfPrice(CatePrice catePrice){
        if(catePrice.getAuthHalfName()==null)
            return;
        try {
            WebElement clickItem = this.waitFindElementByAttr("data-property","游戏版本").findElement(By.xpath(".//span[contains(text(),'"+catePrice.getAuthHalfName()+"')]"));
            clickItem.click();
//            this.waitFindElementByAttr("data-property","语种分类").findElement(By.xpath(".//span[contains(text(),'简体中文')"));
            String price =this.waitFindElementByClass("tb-rmb-num").getText();
            catePrice.setAuthHalfPrice(Double.valueOf(price));
        } catch (Exception e) {
            logger.info("未爬取到认证半年版",e);
        }
    }

    private void setUnAuthPrice(CatePrice catePrice){
        if(catePrice.getUnAuthName()==null)
            return;
        try {
            WebElement clickItem = this.waitFindElementByAttr("data-property","游戏版本").findElement(By.xpath(".//span[contains(text(),'"+catePrice.getUnAuthName()+"')]"));
            clickItem.click();
//            this.waitFindElementByAttr("data-property","语种分类").findElement(By.xpath(".//span[contains(text(),'简体中文')"));
            String price =this.waitFindElementByClass("tb-rmb-num").getText();
            catePrice.setUnAuthPrice(Double.valueOf(price));
        } catch (Exception e) {
            logger.info("未爬取到未认证版",e);
        }
    }
    private void setOfflinePrice(CatePrice catePrice){
        if(catePrice.getOfflineName()==null)
            return;
        try {
            WebElement clickItem = this.waitFindElementByAttr("data-property","游戏版本").findElement(By.xpath(".//span[contains(text(),'"+catePrice.getOfflineName()+"')]"));
            clickItem.click();
//            this.waitFindElementByAttr("data-property","语种分类").findElement(By.xpath(".//span[contains(text(),'简体中文')"));
            String price =this.waitFindElementByClass("tb-rmb-num").getText();
            catePrice.setOfflinePrice(Double.valueOf(price));
        } catch (Exception e) {
            logger.info("未爬取到离线版",e);
        }
    }

    public static void main(String[] args) {
        CatePrice catePrice = new CatePrice();
        catePrice.setKeyword("神海4");
        catePrice.setShop("鑫恺乐软件专营店");
        CatePriceSpider spider = new CatePriceSpider(null);
        spider.initRegister();
        spider.handle(catePrice);

    }
}

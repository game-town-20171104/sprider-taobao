package com.ylfin.spider.Task;

import com.ylfin.spider.component.ShopSpider;
import com.ylfin.spider.service.ShopItemService;
import com.ylfin.spider.utils.DateUtils;
import com.ylfin.spider.vo.SpiderQueue;
import com.ylfin.spider.vo.bean.Shop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShopThread implements Runnable {
    Logger logger = LoggerFactory.getLogger(getClass());
    private SpiderQueue<Shop> queue;
    private int pages;
    ShopItemService shopItemService;

    public ShopThread(SpiderQueue<Shop> queue, ShopItemService shopItemService) {
        this.queue = queue;
        this.shopItemService = shopItemService;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread() + "==start==" + Thread.activeCount());
        ShopSpider spider = new ShopSpider(shopItemService);
//        spider.setDeviceName("iPhone 6");
        if (pages > 0) {
            spider.setTotal(pages);
        }

        spider.initWithProfile();
        spider.setStartDate(DateUtils.format());
        while (true) {
            Shop shop = queue.get();

            if (shop == null) {
                System.out.println("消费结束，准备退出……");
                break;
            }
            System.out.println("开始消费：" + shop);
            try {
                spider.handle(shop);
            } catch (Exception e) {
                logger.error(shop + "爬虫脚本出错了", e);
            }

        }

        spider.quit();
        System.out.println(Thread.currentThread() + "==end==" + Thread.activeCount());
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}

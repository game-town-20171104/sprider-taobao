package com.ylfin.spider.Task;

import com.ylfin.spider.component.SeleniumSpider;
import com.ylfin.spider.service.TaoBaoResultService;
import com.ylfin.spider.utils.DateUtils;
import com.ylfin.spider.vo.SpiderQueue;
import com.ylfin.spider.vo.bean.KeyWords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyWordsThread implements Runnable {
    private Logger logger= LoggerFactory.getLogger(getClass());
    private SpiderQueue<KeyWords> queue;
    private int pages;
    TaoBaoResultService taoBaoResultService;

    public KeyWordsThread(SpiderQueue<KeyWords> queue, TaoBaoResultService taoBaoResultService) {
        this.queue = queue;
        this.taoBaoResultService = taoBaoResultService;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread() + "==start==" + Thread.activeCount());
        SeleniumSpider spider = new SeleniumSpider(taoBaoResultService);
        if (pages > 0) {
            spider.setTotal(pages);
        }

        spider.init();
        spider.setStartDate(DateUtils.format());
        while (true) {
            KeyWords keyword = queue.get();

            if (keyword == null) {
                System.out.println("消费结束，准备退出……");
                break;
            }
            System.out.println("开始消费：" + keyword);
            try {
                spider.jsonHandle(keyword);
            } catch (Exception e) {
                logger.error(keyword + "爬虫脚本出错了", e);
            }

        }

        spider.quit();
        System.out.println(Thread.currentThread() + "==end==" + Thread.activeCount());
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}

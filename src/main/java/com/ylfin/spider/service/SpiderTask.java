package com.ylfin.spider.service;

import com.ylfin.spider.component.SeleniumSpider;
import com.ylfin.spider.utils.DateUtils;
import com.ylfin.spider.vo.KeywordsQueue;
import com.ylfin.spider.vo.bean.KeyWords;
import com.ylfin.spider.vo.bean.Shop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class SpiderTask implements ApplicationRunner {
   static final Logger logger = LoggerFactory.getLogger(SpiderTask.class);

    @Autowired
    KeyWordsService keyWordsService;

    @Autowired
    TaoBaoResultService taoBaoResultService;

    @Autowired
    ShopService shopService;
    @Value("${page.size}")
    private int page ;
    @Value("${thread.size}")
    private int threadSize;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
//        KeywordsQueue queue = new KeywordsQueue();
//        List<KeyWords> keyWords = keyWordsService.findActive();
//        if(CollectionUtils.isEmpty(keyWords)){
//            logger.warn("关键词为空，请配置！");
//            return;
//        }
//        for (KeyWords word : keyWords) {
//            queue.addKeyword(word);
//        }
//
//
//        threadSize = Math.min(threadSize, queue.getSize());
//
//        ThreadPoolExecutor es = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadSize);
//        for (int i = 0; i < threadSize; i++) {
//            InnerThread innerThread =new InnerThread(queue,taoBaoResultService);
//            innerThread.setPages(page);
//            es.submit(innerThread);
//        }
//        es.shutdown();
    }

    static class InnerThread implements Runnable {
        private KeywordsQueue queue;
        private int pages ;
        TaoBaoResultService taoBaoResultService;
        public InnerThread(KeywordsQueue queue, TaoBaoResultService taoBaoResultService) {
            this.queue = queue;
            this.taoBaoResultService =taoBaoResultService;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread() + "==start==" + Thread.activeCount());
            SeleniumSpider spider = new SeleniumSpider(taoBaoResultService);
            if(pages>0){
                spider.setTotal(pages);
            }

            spider.init();
            spider.setStartDate(DateUtils.format());
            while (true) {
                KeyWords keyword = queue.getKeyword();

                if (keyword == null) {
                    System.out.println("消费结束，准备退出……");
                    break;
                }
                System.out.println("开始消费：" + keyword);
                try {
                    spider.jsonHandle(keyword);
                } catch (Exception e) {
                    logger.error(keyword+"爬虫脚本出错了",e);
                }

            }

            spider.quit();
            System.out.println(Thread.currentThread() + "==end==" + Thread.activeCount());
        }

        public void setPages(int pages) {
            this.pages = pages;
        }
    }
}

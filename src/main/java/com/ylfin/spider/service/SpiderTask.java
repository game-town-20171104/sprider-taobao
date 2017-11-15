package com.ylfin.spider.service;

import com.ylfin.spider.component.SeleniumSpider;
import com.ylfin.spider.vo.KeywordsQueue;
import com.ylfin.spider.vo.bean.KeyWords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class SpiderTask {

    @Autowired
    KeyWordsService keyWordsService;

    @Autowired
    TaoBaoResultService taoBaoResultService;
    @Value("${page.size}")
    private int page ;
    @Value("${thread.size}")
    private int threadSize;

    @PostConstruct
    public void start() {
        KeywordsQueue queue = new KeywordsQueue();
        List<KeyWords> keyWords = keyWordsService.query();
        for (KeyWords word : keyWords) {
            queue.addKeyword(word);
        }


        threadSize = Math.min(threadSize, queue.getSize());

        ThreadPoolExecutor es = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadSize);
        for (int i = 0; i < threadSize; i++) {
            InnerThread innerThread =new InnerThread(queue,taoBaoResultService);
            innerThread.setPages(page);
            es.submit(innerThread);
        }
        es.shutdown();
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
            while (true) {
                KeyWords keyword = queue.getKeyword();

                if (keyword == null) {
                    System.out.println("消费结束，准备退出……");
                    break;
                }
                System.out.println("开始消费：" + keyword);
                spider.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                spider.jsonHandle(keyword);

            }

            spider.quit();
            System.out.println(Thread.currentThread() + "==end==" + Thread.activeCount());
        }

        public void setPages(int pages) {
            this.pages = pages;
        }
    }
}

package com.ylfin.spider.service;

import com.ylfin.spider.component.SeleniumSpider;
import com.ylfin.spider.vo.KeywordsQueue;
import com.ylfin.spider.vo.bean.KeyWords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class SpiderTask {

    @Autowired
    KeyWordsService keyWordsService;

    @Autowired
    TaoBaoResultService taoBaoResultService;

    @PostConstruct
    public void start() {
        int threadSize = 2;
        KeywordsQueue queue = new KeywordsQueue();
        List<KeyWords> keyWords = keyWordsService.query();
        for (KeyWords word : keyWords) {
            queue.addKeyword(word);
        }


        threadSize = Math.min(threadSize, queue.getSize());

        ThreadPoolExecutor es = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadSize);
        for (int i = 0; i < threadSize; i++) {
            es.submit(new InnerThread(queue,taoBaoResultService));
        }
//        es.shutdown();
    }

    static class InnerThread implements Runnable {
        private KeywordsQueue queue;
        TaoBaoResultService taoBaoResultService;
        public InnerThread(KeywordsQueue queue, TaoBaoResultService taoBaoResultService) {
            this.queue = queue;
            this.taoBaoResultService =taoBaoResultService;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread() + "==start==" + Thread.activeCount());
            SeleniumSpider spider = new SeleniumSpider(taoBaoResultService);
            spider.init();
            while (true) {
                KeyWords keyword = queue.getKeyword();
                if (keyword == null) {
                    System.out.println("消费结束，准备退出……");
                    break;
                }
                System.out.println("开始消费：" + keyword);
                spider.jsonHandle(keyword);

            }

            spider.quit();
            System.out.println(Thread.currentThread() + "==end==" + Thread.activeCount());
        }
    }
}

package com.ylfin.spider;

import com.ylfin.spider.vo.KeywordsQueue;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SpiderClient {
    //线程数,不需要太多，建议2-3个，每打开一个等于打开一个浏览器，会有初始化耗时和内存消耗
    private static int threadSize = 2;

    public static void main(String[] args) {
        KeywordsQueue queue = new KeywordsQueue();
        queue.addKeyword("小米路由");
        queue.addKeyword("汽车垫");
        queue.addKeyword("iPhoneX");
        queue.addKeyword("刺客信条 起源 ps4");


        threadSize = Math.max(threadSize,queue.getSize());

        ThreadPoolExecutor es   =(ThreadPoolExecutor)Executors.newFixedThreadPool(threadSize);
        for(int i=0;i<threadSize;i++){
            es.submit(new InnerThread(queue));
        }
        es.shutdown();
    }




    static class InnerThread implements Runnable{
        private KeywordsQueue queue;
        public InnerThread(KeywordsQueue queue) {
            this.queue =queue;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread()+"==start=="+Thread.activeCount());
            SeleniumSpider spider = new SeleniumSpider();
            spider.init();
            while (true){
                String keyword =queue.getKeyword();
                if(keyword==null){
                    System.out.println("消费结束，准备退出……");
                    break;
                }
                System.out.println("开始消费："+keyword);
                spider.jsonHandle(keyword);

            }

            spider.quit();
            System.out.println(Thread.currentThread()+"==end=="+Thread.activeCount());
        }
    }
}



package com.ylfin.spider.Task;

import com.ylfin.spider.component.SearchSpider;
import com.ylfin.spider.service.ProxyService;
import com.ylfin.spider.vo.SpiderQueue;
import com.ylfin.spider.vo.bean.SearchKeyWords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchThread implements  Runnable {
    Logger logger = LoggerFactory.getLogger(getClass());
    private SpiderQueue<SearchKeyWords> queue;
    private ProxyService proxyService;

    public SearchThread(SpiderQueue<SearchKeyWords> queue, ProxyService proxyService) {
        this.queue = queue;
        this.proxyService = proxyService;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread() + "==start==" + Thread.activeCount());
        SearchSpider spider = new SearchSpider();
        while (true) {
            SearchKeyWords keyWords = queue.get();
            if (keyWords == null) {
                System.out.println("消费结束，准备退出……");
                break;
            }
            String ip = proxyService.findProxy();
            spider.setHttpProxy(ip);
            spider.setHeadless(false); //默认是关闭的
            spider.initWithProfile();
            System.out.println("开始消费：" + keyWords);
            try {
                spider.handle(keyWords);
            } catch (Exception e) {
                logger.error(keyWords + "脚本出错了,浪费了一个IP地址："+ip+"回收利用下把亲【用ip替换 proxyService.findProxy()】", e);
            }

        }

        spider.quit();
        System.out.println(Thread.currentThread() + "==end==" + Thread.activeCount());
    }
}

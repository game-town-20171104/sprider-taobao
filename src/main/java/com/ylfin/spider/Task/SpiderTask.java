package com.ylfin.spider.Task;

import com.ylfin.spider.register.RegisterFactory;
import com.ylfin.spider.register.SonyRegister;
import com.ylfin.spider.register.service.MailService;
import com.ylfin.spider.register.service.SonyService;
import com.ylfin.spider.register.vo.bean.MailBean;
import com.ylfin.spider.register.vo.bean.SonyBean;
import com.ylfin.spider.service.*;
import com.ylfin.spider.vo.SpiderQueue;
import com.ylfin.spider.vo.bean.KeyWords;
import com.ylfin.spider.vo.bean.SearchKeyWords;
import com.ylfin.spider.vo.bean.Shop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
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
    ShopItemService shopItemService;

    @Autowired
    ShopService shopService;

    @Autowired
    ProxyService proxyService;

    @Autowired
    SearchKeywordService searchKeywordService;

    @Autowired
    MailService mailService;

    @Autowired
    RegisterFactory registerFactory;

    @Autowired
    SonyService sonyService;


    @Value("${page.size}")
    private int page;
    @Value("${thread.size}")
    private int threadSize;
    @Value("${spider.model}")
    private int model;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {

        ThreadPoolExecutor es = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadSize);

        if (model == 2) {
            submitShopSpider(es);
        } else if (model == 1) {
            submitSpiderKeywords(es);
        } else if (model == 3) {
            submitKeywordSearch(es);
        } else if (model == 4) {
            submitMail163(es);
        } else if (model == 5) {
            submitSony(es);
        }

        es.shutdown();
        //守护线程，如果所有线程都结束，退出应用
        Thread daemon = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000L);
                    if (es.isTerminated()) {
                        System.out.println("准备退出……");
                        System.exit(0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        daemon.setDaemon(true);
        daemon.start();

    }
    private void submitSpiderKeywords(ExecutorService es){
        SpiderQueue<KeyWords> queue = new SpiderQueue<>();
        List<KeyWords> keyWords = keyWordsService.findActive();
        if (!CollectionUtils.isEmpty(keyWords)) {
            keyWords.forEach(queue::add);
        } else {
            logger.info("关键词配置为空！");
        }
        for (int i = 0; i < threadSize; i++) {
            KeyWordsThread innerThread = new KeyWordsThread(queue, taoBaoResultService);
            innerThread.setPages(page);
            es.submit(innerThread);
        }
    }


    /**
     * 店铺爬取
     * @param es
     */
    private void submitShopSpider(ExecutorService es){
        SpiderQueue<Shop> shopQueue = new SpiderQueue<>();
        List<Shop> shops = shopService.findActive();
        if (!CollectionUtils.isEmpty(shops)) {
            shops.forEach(shopQueue::add);
        } else {
            logger.info("店铺配置为空");
        }

        ShopThread shopThread = new ShopThread(shopQueue, shopItemService);
        shopThread.setPages(page);
        es.submit(shopThread);
    }

    /**
     * 关键词搜索
     * @param es
     */
    private void submitKeywordSearch(ExecutorService es){
        logger.info("keywords search start");
        SpiderQueue<SearchKeyWords> searchQueue = new SpiderQueue<>();
        List<SearchKeyWords> searchKeyWords = searchKeywordService.findActive();
        if (CollectionUtils.isEmpty(searchKeyWords)) {
            logger.info("搜索配置为空！");
            return;
        }
        searchKeyWords.forEach(searchQueue::add);
        SearchThread searchThread = new SearchThread(searchQueue, proxyService);
        es.submit(searchThread);
    }

    /**
     * 网易注册
     * @param es
     */
    private void submitMail163(ExecutorService es){
        logger.info("mail163 register  start");
        SpiderQueue<MailBean> mailQueue = new SpiderQueue<>();
        List<MailBean> mails = mailService.findActiveAndUnSucc();
        if (CollectionUtils.isEmpty(mails)) {
            logger.info("注册配置为空！");
            return;
        }
        mails.forEach(mailQueue::add);
        Mail163Thread searchThread = new Mail163Thread(mailQueue,registerFactory);
        es.submit(searchThread);
    }

    /**
     * 索尼注册
     * @param es
     */
    private void submitSony(ExecutorService es){
        logger.info("sony register  start");
        SpiderQueue<SonyBean> queue = new SpiderQueue<>();
        List<SonyBean> mails = sonyService.findActiveAndUnSucc();
        if (CollectionUtils.isEmpty(mails)) {
            logger.info("注册配置为空！");
            return;
        }
        mails.forEach(queue::add);
        SonyRegisterThread sonyThread = new SonyRegisterThread(queue,registerFactory);
        es.submit(sonyThread);
    }


}

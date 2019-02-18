package com.ylfin.spider.Task;

import com.ylfin.spider.account.service.CheckMissionService;
import com.ylfin.spider.account.vo.bean.CheckMission;
import com.ylfin.spider.cateprice.service.CatePriceService;
import com.ylfin.spider.cateprice.vo.bean.CatePrice;
import com.ylfin.spider.eshop.EshopSpider;
import com.ylfin.spider.register.RegisterFactory;
import com.ylfin.spider.register.enums.RegisterType;
import com.ylfin.spider.register.service.MailService;
import com.ylfin.spider.register.service.NintendoService;
import com.ylfin.spider.register.service.NitendoPwdService;
import com.ylfin.spider.register.service.SonyService;
import com.ylfin.spider.register.service.impl.NintendoServiceImpl;
import com.ylfin.spider.register.vo.NintendoPwdVO;
import com.ylfin.spider.register.vo.bean.MailBean;
import com.ylfin.spider.register.vo.bean.NintendoBean;
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

import java.util.ArrayList;
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

    @Autowired
    CatePriceService catePriceService;

    @Autowired
    NintendoService nintendoService;

    @Autowired
    NitendoPwdService nitendoPwdService;

    @Autowired
    CheckMissionService checkMissionService;

    @Value("${page.size}")
    private int page;
    @Value("${thread.size}")
    private int threadSize;
    @Value("${spider.model}")
    private int model;
    @Value("${nintendo.excel}")
    private String excelPath;
    @Autowired
    EshopTask eshopTask;
    public  final ThreadPoolExecutor es = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {

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
        }else if (model == 6) {
            submitCatePrice(es);
        }else if (model == 7) {
            submitNintendo(es);
        }else if(model ==8 ){
            //立即执行一次
//            es.shutdown();
            eshopTask.dailyPriceSpider();
            return;
        }else if (model == 9) {
            submitAoi(es);
        }else if (model == 10) {
            submitNintendoPwd(es);
        }else if (model == 11) {
            submitMission(es);
        }else{
            System.out.println("暂无任务……");
//            return;
            es.submit(() -> {
                System.out.println("---任务测试开始-----");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("---任务测试结束-----");
            });
        }

//        es.shutdown();
//        守护线程，如果所有线程都结束，退出应用
        Thread daemon = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000L);
//                    if (es.isTerminated()) {
//                        System.out.println("准备退出……");
//                        System.exit(0);
//                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        daemon.setDaemon(true);
        daemon.start();

    }

    public void setModel(int model) {
        this.model = model;
    }

    private void submitSpiderKeywords(ExecutorService es) {
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
     *
     * @param es
     */
    private void submitShopSpider(ExecutorService es) {
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
     *
     * @param es
     */
    private void submitKeywordSearch(ExecutorService es) {
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
     *
     * @param es
     */
    private void submitMail163(ExecutorService es) {
        logger.info("mail163 register  start");
        SpiderQueue<MailBean> mailQueue = new SpiderQueue<>();
        List<MailBean> mails = mailService.findActiveAndUnSucc();
        if (CollectionUtils.isEmpty(mails)) {
            logger.info("注册配置为空！");
            return;
        }
        mails.forEach(mailQueue::add);
        ReOpenThread<MailBean> searchThread = new ReOpenThread(mailQueue, registerFactory.getRegister(RegisterType.mail163));
        es.submit(searchThread);
    }

    /**
     * 索尼注册
     *
     * @param es
     */
    private void submitSony(ExecutorService es) {
        logger.info("sony register  start");
        SpiderQueue<SonyBean> queue = new SpiderQueue<>();
        List<SonyBean> mails = sonyService.findActiveAndUnSucc();
        if (CollectionUtils.isEmpty(mails)) {
            logger.info("注册配置为空！");
            return;
        }
        mails.forEach(queue::add);
        SonyRegisterThread sonyThread = new SonyRegisterThread(queue, registerFactory);
        es.submit(sonyThread);
    }

    /**
     * AOI 邮箱注册
     *
     * @param es
     */
    private void submitAoi(ExecutorService es) {
        logger.info("aoi register  start");
        SpiderQueue<MailBean> mailQueue = new SpiderQueue<>();
        List<MailBean> mails = mailService.findActiveAndUnSucc();
        if (CollectionUtils.isEmpty(mails)) {
            logger.info("注册配置为空！");
            return;
        }
        mails.forEach(mailQueue::add);
        MailAoiThread searchThread = new MailAoiThread(mailQueue, registerFactory);
        es.submit(searchThread);
    }

    /**
     * 分类价格爬取
     * @param es
     */
    private void submitCatePrice(ExecutorService es) {
        SpiderQueue<CatePrice> queue = new SpiderQueue<>();
        List<CatePrice> catePrices = catePriceService.findActiveAndUnSuccess();
        if (CollectionUtils.isEmpty(catePrices)) {
            logger.info("分类价格配置为空！");
            return;
        }
        catePrices.forEach(queue::add);
        CatePriceThread catePriceThread = new CatePriceThread(queue, registerFactory);
        es.submit(catePriceThread);
    }



    /**
     * 任天堂注册
     *
     * @param es
     */
    private void submitNintendo(ExecutorService es) {
        logger.info("sony nitendo  start");
        SpiderQueue<NintendoBean> queue = new SpiderQueue<>();
        List<NintendoBean> mails = nintendoService.findActiveAndUnSucc();
        if (CollectionUtils.isEmpty(mails)) {
            logger.info("注册配置为空！");
            return;
        }
        mails.forEach(queue::add);
        NintendoThread nintendoThread = new NintendoThread(queue, registerFactory);
        es.submit(nintendoThread);
    }


    /**
     * 任天堂修改密码注册
     *
     * @param es
     */
    private void submitNintendoPwd(ExecutorService es) {
        logger.info("sony nitendo pwd start");
        SpiderQueue<NintendoPwdVO> queue = new SpiderQueue<>();
        List<NintendoPwdVO> mails = nitendoPwdService.getExcelList(excelPath);
        if (CollectionUtils.isEmpty(mails)) {
            logger.info("配置为空！");
            return;
        }
        mails.forEach(queue::add);
        NintendoPwdThread nintendoThread = new NintendoPwdThread(queue, registerFactory);
        es.submit(nintendoThread);
    }

    private void submitMission(ExecutorService es) {
        logger.info("sony nitendo chekmission start");
        SpiderQueue<CheckMission> queue = new SpiderQueue<>();
        List<CheckMission> mails = checkMissionService.findActiveAndUnSuccess();
        if (CollectionUtils.isEmpty(mails)) {
            logger.info("任务为空！");
            return;
        }
        mails.forEach(queue::add);
        CheckMissionTask nintendoThread = new CheckMissionTask(queue, registerFactory);
        es.submit(nintendoThread);
    }


}

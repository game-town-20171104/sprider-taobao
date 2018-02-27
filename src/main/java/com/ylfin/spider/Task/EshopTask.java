package com.ylfin.spider.Task;

import com.ylfin.spider.eshop.EshopSpider;
import com.ylfin.spider.eshop.service.EshopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Configuration
@Component
@EnableScheduling
public class EshopTask {

    @Autowired
    private EshopService eshopService;

    @Value("${spider.model}")
    private int model;

    /**
     * 0点，6点，18点各执行异常
      */
    @Scheduled(cron = "0 0 0,6,18 * * ? ")
//    @Scheduled(cron = "0 0/2 * * * ?")
    public void dailyPriceSpider(){
        if(model!=8){
            return;
        }
        EshopSpider eshopSpider = new EshopSpider(eshopService);
        eshopSpider.initRegister();
        eshopSpider.handle(null);
        eshopSpider.quit();
    }
}

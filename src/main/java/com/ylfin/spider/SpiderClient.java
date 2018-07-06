package com.ylfin.spider;

import com.ylfin.spider.component.SeleniumSpider;
import com.ylfin.spider.vo.KeywordsQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
public class SpiderClient {


    public static void main(String[] args) {

        SpringApplication.run(SpiderClient.class, args);
    }





}



package com.ylfin.spider;

import com.ylfin.spider.service.TaoBaoResultService;
import com.ylfin.spider.vo.bean.TaoBaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BeanTest {
    @Autowired
    TaoBaoResultService taoBaoResultService;
//    @PostConstruct
    public void test(){
        TaoBaoResult taoBaoResult = new TaoBaoResult();
        taoBaoResult.setKeywordsId(1L);
        taoBaoResult.setShopName("测试");
        taoBaoResultService.save(taoBaoResult);



    }
}

package com.ylfin.spider;

import com.ylfin.spider.service.KeyWordsService;
import com.ylfin.spider.service.TaoBaoResultService;
import com.ylfin.spider.vo.TaobaoVO;
import com.ylfin.spider.vo.bean.KeyWords;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class TaoBaoResultServiceImplTest extends AbstractSpringTest {

    @Autowired
    KeyWordsService keyWordsService ;

    @Autowired
    TaoBaoResultService taoBaoResultService;

    @Test
    public void keyWordsTest(){
      List<KeyWords> list = keyWordsService.query();
        System.out.println(list);
    }


    @Test
    public void findActive(){
        List<KeyWords> list = keyWordsService.findActive();
        System.out.println(list);
    }


    @Test
    public void batchInserTest(){
        List<TaobaoVO> list = new ArrayList<>();
        TaobaoVO t = new TaobaoVO();
        t.setKeywordId(1);
        t.setShop("xxx");
        list.add(t);
        taoBaoResultService.batchSave(list);
    }
}

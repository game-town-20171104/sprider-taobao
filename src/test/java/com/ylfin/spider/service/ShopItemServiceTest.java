package com.ylfin.spider.service;

import com.ylfin.spider.AbstractSpringTest;
import com.ylfin.spider.vo.bean.ShopItem;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


public class ShopItemServiceTest extends AbstractSpringTest {

    @Autowired
    ShopItemService shopItemService;

    @Test
    public void batchInsert() throws Exception {
        List<ShopItem> list = new ArrayList<>();
        ShopItem  item = new ShopItem();
        item.setShopId(1L);
        item.setName("测试");
        item.setPrice(2.0);
        item.setSales(1000);
        item.setRates(100);
        list.add(item);

        shopItemService.batchInsert(list);
    }



}

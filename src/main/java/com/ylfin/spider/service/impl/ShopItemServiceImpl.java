package com.ylfin.spider.service.impl;

import com.ylfin.spider.mapper.ShopItemDao;
import com.ylfin.spider.service.ShopItemService;
import com.ylfin.spider.vo.bean.ShopItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopItemServiceImpl implements ShopItemService {

    @Autowired
    ShopItemDao shopItemDao;

    @Override
    public void batchInsert(List<ShopItem> shopItemVOS) {
        shopItemDao.batchInsert(shopItemVOS);
    }
}

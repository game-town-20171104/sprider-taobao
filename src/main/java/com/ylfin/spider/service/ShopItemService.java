package com.ylfin.spider.service;

import com.ylfin.spider.vo.bean.ShopItem;

import java.util.List;

public interface ShopItemService {

   void batchInsert(List<ShopItem> shopItemVOS);
}

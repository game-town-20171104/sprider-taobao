package com.ylfin.spider.service;

import com.ylfin.spider.vo.bean.Shop;

import java.util.List;

public interface ShopService {

    List<Shop> findActive();
}

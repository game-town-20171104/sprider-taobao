package com.ylfin.spider.cateprice.service;

import com.ylfin.spider.cateprice.vo.bean.CatePrice;

import java.util.List;

public interface CatePriceService {

    List<CatePrice> findActiveAndUnSuccess();

    void update(CatePrice catePrice);
}

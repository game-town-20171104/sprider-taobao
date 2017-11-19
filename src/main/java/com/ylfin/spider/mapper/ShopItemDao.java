package com.ylfin.spider.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ylfin.spider.vo.bean.ShopItem;

import java.util.List;

public interface ShopItemDao extends BaseMapper<ShopItem> {

    void batchInsert(List<ShopItem> taoBaoResultList);
}

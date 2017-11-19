package com.ylfin.spider.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ylfin.spider.mapper.ShopDao;
import com.ylfin.spider.service.ShopService;
import com.ylfin.spider.vo.bean.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    ShopDao shopDao;

    public  List<Shop> findActive(){
        return  shopDao.selectList(  new EntityWrapper<Shop>().eq("active",true));
    }
}

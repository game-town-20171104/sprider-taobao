package com.ylfin.spider.cateprice.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ylfin.spider.cateprice.mapper.CatePriceDao;
import com.ylfin.spider.cateprice.service.CatePriceService;
import com.ylfin.spider.cateprice.vo.bean.CatePrice;
import com.ylfin.spider.register.vo.bean.MailBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatePriceServiceImpl implements CatePriceService {

    @Autowired
    CatePriceDao catePriceDao;

    @Override
    public List<CatePrice> findActiveAndUnSuccess() {
        return  catePriceDao.selectList(  new EntityWrapper<CatePrice>().eq("active",true).eq("success",false));
    }

    @Override
    public void update(CatePrice catePrice) {
        catePriceDao.updateById(catePrice);
    }
}

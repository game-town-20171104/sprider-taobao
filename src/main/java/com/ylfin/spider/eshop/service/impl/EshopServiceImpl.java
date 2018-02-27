package com.ylfin.spider.eshop.service.impl;

import com.ylfin.spider.eshop.mapper.EshopDao;
import com.ylfin.spider.eshop.service.EshopService;
import com.ylfin.spider.eshop.vo.bean.EshopBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EshopServiceImpl implements EshopService {

    @Autowired
    private EshopDao eshopDao;

    @Override
    public void batchSave(List<EshopBean> list) {
        eshopDao.batchInsert(list);
    }
}

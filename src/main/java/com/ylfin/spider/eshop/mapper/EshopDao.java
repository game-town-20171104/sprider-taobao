package com.ylfin.spider.eshop.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ylfin.spider.eshop.vo.bean.EshopBean;
import java.util.*;

public interface EshopDao extends BaseMapper<EshopBean> {

    void batchInsert(List<EshopBean> beanList);
}

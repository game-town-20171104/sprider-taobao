package com.ylfin.spider.register.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ylfin.spider.register.enums.SonyRegisterStep;
import com.ylfin.spider.register.mapper.SonyDao;
import com.ylfin.spider.register.service.SonyService;
import com.ylfin.spider.register.vo.bean.SonyBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SonyServiceImpl implements SonyService {

    @Autowired
    private SonyDao sonyDao;
    @Override
    public List<SonyBean> findActiveAndUnSucc() {
        return sonyDao.selectList(  new EntityWrapper<SonyBean>().eq("active",true).eq("success",false));
    }

    @Override
    public void setSuccess(Long id) {
        SonyBean sonyBean =new SonyBean();
        sonyBean.setId(id);
        sonyBean.setSuccess(true);
        sonyDao.updateById(sonyBean);
    }

    @Override
    public void updateStep(Long id, SonyRegisterStep step) {
        SonyBean sonyBean =new SonyBean();
        sonyBean.setId(id);
        sonyBean.setStep(step.getCode());
        sonyDao.updateById(sonyBean);
    }
}

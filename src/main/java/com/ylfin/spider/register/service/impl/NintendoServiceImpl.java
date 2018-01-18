package com.ylfin.spider.register.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ylfin.spider.register.mapper.NintendoDao;
import com.ylfin.spider.register.service.NintendoService;
import com.ylfin.spider.register.vo.bean.NintendoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NintendoServiceImpl implements NintendoService {
    @Autowired
    NintendoDao nintendoDao;
    @Override
    public List<NintendoBean> findActiveAndUnSucc() {
        return  nintendoDao.selectList(  new EntityWrapper<NintendoBean>().eq("active",true).eq("success",false));
    }



    @Override
    public void update(NintendoBean nintendoBean) {
        nintendoBean.setSuccess(true);
        nintendoDao.updateById(nintendoBean);
    }
}

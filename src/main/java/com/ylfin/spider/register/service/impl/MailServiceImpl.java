package com.ylfin.spider.register.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ylfin.spider.register.mapper.MailDao;
import com.ylfin.spider.register.service.MailService;
import com.ylfin.spider.register.vo.bean.MailBean;
import com.ylfin.spider.vo.bean.KeyWords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    MailDao mailDao;
    @Override
    public List<MailBean> findActiveAndUnSucc() {
        return  mailDao.selectList(  new EntityWrapper<MailBean>().eq("active",true).eq("success",false));
    }

    public MailBean findByEmail(String email){
        MailBean mailBean = new MailBean();
        mailBean.setEmail(email);
        return  mailDao.selectOne(mailBean);
    }

    @Override
    public void update(MailBean mailBean) {
        mailDao.updateById(mailBean);
    }

    @Override
    public void setSuccess(Long id) {
        MailBean mailBean =new MailBean();
        mailBean.setId(id);
        mailBean.setSuccess(true);
        mailDao.updateById(mailBean);
    }


}

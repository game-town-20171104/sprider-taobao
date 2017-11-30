package com.ylfin.spider.register.service.impl;

import com.ylfin.spider.AbstractSpringTest;
import com.ylfin.spider.register.mapper.MailDao;
import com.ylfin.spider.register.service.MailService;
import com.ylfin.spider.register.vo.bean.MailBean;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class MailServiceImplTest extends AbstractSpringTest{
    @Autowired
    MailService mailService;
    @Autowired
    MailDao mailDao;

    @Test
    public void setSuccess() throws Exception {
        MailBean mailBean = new MailBean();
        mailBean.setPhone("136161562870");
        mailBean.setUsername("xxx");
        mailBean.setPassword("123");
        mailBean.setEmail("xxx@126.com");
       Integer i= mailDao.insert(mailBean);
//        System.out.println(i);
////
//        MailBean mailBean = new MailBean();
//        mailBean.setId(1L);
//        mailBean.setSuccess(false);

//        mailService.setSuccess(936111248674684929L);
    }

}
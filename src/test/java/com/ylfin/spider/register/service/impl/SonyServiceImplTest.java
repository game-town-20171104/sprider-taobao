package com.ylfin.spider.register.service.impl;

import com.ylfin.spider.AbstractSpringTest;
import com.ylfin.spider.register.enums.SonyRegisterStep;
import com.ylfin.spider.register.mapper.SonyDao;
import com.ylfin.spider.register.service.SonyService;
import com.ylfin.spider.register.vo.bean.SonyBean;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

import static org.junit.Assert.*;

public class SonyServiceImplTest extends AbstractSpringTest {

    @Autowired
    private SonyDao sonyDao;

    @Autowired
    private SonyService sonyService;


    
    @Test
    public void findActiveAndUnSucc() throws Exception {
        Assert.notEmpty( sonyService.findActiveAndUnSucc(),"长度为空");
    }

    @Test
    public void setSuccess() throws Exception {
        sonyService.setSuccess(1L);
    }

    @Test
    public void updateStep() throws Exception {
        sonyService.updateStep(1L, SonyRegisterStep.STEP_02);
    }

}
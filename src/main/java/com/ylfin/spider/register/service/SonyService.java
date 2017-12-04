package com.ylfin.spider.register.service;

import com.ylfin.spider.register.enums.SonyRegisterStep;
import com.ylfin.spider.register.vo.bean.SonyBean;

import java.util.List;

public interface SonyService {

    List<SonyBean> findActiveAndUnSucc();

    void setSuccess(Long id);

    void updateStep(Long id, SonyRegisterStep step);
}

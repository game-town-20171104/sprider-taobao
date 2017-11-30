package com.ylfin.spider.register.service;

import com.ylfin.spider.register.vo.bean.MailBean;

import java.util.List;

public interface MailService {
    List<MailBean> findActiveAndScuess();

   void setSuccess(Long id);
}

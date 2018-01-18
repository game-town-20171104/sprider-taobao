package com.ylfin.spider.register.service;

import com.ylfin.spider.register.vo.bean.MailBean;
import com.ylfin.spider.register.vo.bean.NintendoBean;

import java.util.List;

public interface NintendoService {
    List<NintendoBean> findActiveAndUnSucc();

    void update(NintendoBean nintendoBean);

}

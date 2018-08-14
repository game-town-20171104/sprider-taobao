package com.ylfin.spider.register.service;

import com.ylfin.spider.register.vo.NintendoPwdVO;
import com.ylfin.spider.register.vo.bean.NintendoBean;

import java.util.List;

public interface NitendoPwdService {

    List<NintendoPwdVO> getExcelList(String file);

    void save2Excel(NintendoPwdVO netdoVO);
}

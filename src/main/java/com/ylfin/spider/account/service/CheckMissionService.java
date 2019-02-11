package com.ylfin.spider.account.service;

import com.baomidou.mybatisplus.service.IService;
import com.ylfin.spider.account.vo.bean.CheckMission;
import com.ylfin.spider.cateprice.vo.bean.CatePrice;

import java.util.List;

public interface CheckMissionService extends IService<CheckMission> {

    List<CheckMission> findActiveAndUnSuccess();
}

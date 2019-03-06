package com.ylfin.spider.account.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ylfin.spider.account.mapper.CheckMissionDao;
import com.ylfin.spider.account.service.CheckMissionService;
import com.ylfin.spider.account.vo.bean.CheckMission;
import com.ylfin.spider.cateprice.vo.bean.CatePrice;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: godslhand
 * @date: 2019/2/11
 * @description:
 */
@Service
public class CheckMissionServiceImpl extends ServiceImpl<CheckMissionDao, CheckMission> implements CheckMissionService {
    @Override
    public List<CheckMission> findActiveAndUnSuccess() {
        return   baseMapper.selectList(  new EntityWrapper<CheckMission>().eq("active",true).ne("result",1));
    }


    public  void getMissionPage(){
    }
}

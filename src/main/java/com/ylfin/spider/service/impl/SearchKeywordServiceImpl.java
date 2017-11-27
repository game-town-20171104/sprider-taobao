package com.ylfin.spider.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ylfin.spider.mapper.SearchKeyWordDao;
import com.ylfin.spider.service.SearchKeywordService;
import com.ylfin.spider.vo.bean.SearchKeyWords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchKeywordServiceImpl implements SearchKeywordService {

    @Autowired
    SearchKeyWordDao searchKeyWordDao;

    @Override
    public  List<SearchKeyWords> findActive() {
       List<SearchKeyWords> list= searchKeyWordDao.selectList(  new EntityWrapper<SearchKeyWords>().eq("active",true));
       return list;
    }
}

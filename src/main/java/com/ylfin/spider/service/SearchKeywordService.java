package com.ylfin.spider.service;

import com.ylfin.spider.vo.bean.SearchKeyWords;

import java.util.List;

public interface SearchKeywordService {

    List<SearchKeyWords> findActive();
}

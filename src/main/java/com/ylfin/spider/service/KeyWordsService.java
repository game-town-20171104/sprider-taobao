package com.ylfin.spider.service;

import com.ylfin.spider.vo.bean.KeyWords;

import java.util.List;

public interface KeyWordsService {

     List<KeyWords> query();

     List<KeyWords> findActive();
}

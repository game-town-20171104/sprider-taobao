package com.ylfin.spider.service;

import com.ylfin.spider.vo.TaobaoVO;
import com.ylfin.spider.vo.bean.TaoBaoResult;

import java.util.List;

public interface TaoBaoResultService {

     void save(TaoBaoResult result);

     void batchSave(List<TaobaoVO> taoBaoResultList);
}

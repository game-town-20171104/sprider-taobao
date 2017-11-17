package com.ylfin.spider.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ylfin.spider.vo.bean.TaoBaoResult;

import java.util.List;

public interface TaoBaoResultDao extends BaseMapper<TaoBaoResult> {

     void batchInsert(List<TaoBaoResult> taoBaoResultList);
}

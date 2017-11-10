package com.ylfin.spider.service.impl;

import com.ylfin.spider.service.TaoBaoResultService;
import com.ylfin.spider.vo.TaobaoVO;
import com.ylfin.spider.vo.bean.TaoBaoResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaoBaoResultServiceImpl implements TaoBaoResultService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void save(TaoBaoResult result) {

        jdbcTemplate.update("insert into taobao_result (keywords_id,comment_count," +
                "data_str,detail_url,price,shop_name,num,title,location) values(?,?,?,?,?,?,?,?,?)", new Object[]{
                result.getKeywordsId(), result.getCommentCount(), result.getDateStr(), result.getDetailUrl(),
                result.getPrice(), result.getShopName(), result.getNum(), result.getTitle(), result.getLocation()
        });
    }

    @Override
    public void batchSave(List<TaobaoVO> taoBaoResultList) {

        try {
            String dataStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            List<Object[]> objects = new ArrayList<>();
            for (TaobaoVO taobaoVO:taoBaoResultList){
                Object[] ob =new Object[]{
                        taobaoVO.getKeywordId(),taobaoVO.getCommentCount(),dataStr,taobaoVO.getDetailUrl(),
                        taobaoVO.getViewPrice(),taobaoVO.getShop(),taobaoVO.getViewSales(),
                        taobaoVO.getRawTitle(),taobaoVO.getArea()};

                objects.add(ob);
            }

            jdbcTemplate.batchUpdate("insert into taobao_result (keywords_id,comment_count," +
                    "data_str,detail_url,price,shop_name,num,title,location) values(?,?,?,?,?,?,?,?,?)",objects );
        } catch (Exception e){
            logger.error("保存数据库失败",e);
        }
    }


    public static void main(String[] args) {
        List<Object[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").stream()
                .map(name -> name.split(" "))
                .collect(Collectors.toList());

        System.out.println(splitUpNames);
    }
}

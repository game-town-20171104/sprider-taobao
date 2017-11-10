package com.ylfin.spider.service.impl;

import com.alibaba.fastjson.parser.Keywords;
import com.ylfin.spider.service.KeyWordsService;
import com.ylfin.spider.vo.bean.KeyWords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class KeyWordsServiceImpl implements KeyWordsService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    Logger logger  = LoggerFactory.getLogger(getClass());

    @Override
    public List<KeyWords> query(){
        String sql = "select id,title from keywords";

        List<KeyWords> keywordsList = jdbcTemplate.query(sql,new KeyWordsMapper() );
        return keywordsList;
    }

    protected class KeyWordsMapper implements RowMapper<KeyWords>{

        @Override
        public KeyWords mapRow(ResultSet rs, int i) throws SQLException {
            return new KeyWords(rs.getLong(1),rs.getString(2));
        }
    }
}

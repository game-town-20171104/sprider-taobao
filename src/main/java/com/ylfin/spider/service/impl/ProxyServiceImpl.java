package com.ylfin.spider.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ylfin.spider.mapper.ProxyServerDao;
import com.ylfin.spider.service.ProxyService;
import com.ylfin.spider.utils.OkHttpUtil;
import com.ylfin.spider.vo.bean.ProxyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
public class ProxyServiceImpl implements ProxyService {

    @Autowired
    ProxyServerDao proxyServerDao;

    @Override
    public String findProxy() {

        List<ProxyServer> list = proxyServerDao.selectList(new EntityWrapper<>());
        if(CollectionUtils.isEmpty(list)){
           throw new RuntimeException("获取服务器地址为空！请先配置url");
        }

        String ip;
        String url =list.get(0).getUrl();
        try {
          ip= OkHttpUtil.get(url,new HashMap<>(),false);
        } catch (IOException e) {
           throw new RuntimeException("获取代理请求失败",e);
        }
        if(StringUtils.isEmpty(ip)){
            throw new RuntimeException("获取的代理地址为空!");
        }
        return ip.trim();
    }
}

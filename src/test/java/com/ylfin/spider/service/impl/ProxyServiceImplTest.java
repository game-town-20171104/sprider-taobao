package com.ylfin.spider.service.impl;

import com.ylfin.spider.AbstractSpringTest;
import com.ylfin.spider.mapper.ProxyServerDao;
import com.ylfin.spider.service.ProxyService;
import com.ylfin.spider.vo.bean.ProxyServer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ProxyServiceImplTest extends AbstractSpringTest {

    @Autowired
    ProxyService proxyService;

    @Autowired
    ProxyServerDao proxyServerDao;
    @Test
    public void findProxy() throws Exception {
//        ProxyServer proxyServer =new ProxyServer();
//        proxyServer.setUrl("http://www.baidu.com");
//        proxyServerDao.insert(proxyServer);
//       String url = proxyService.findProxy();
        System.out.println("171.42.201.118:5374");
    }

}
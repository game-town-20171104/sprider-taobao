package com.ylfin.spider.vo.bean;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("proxy_server")
public class ProxyServer {
    private int id;
    private String url;
}

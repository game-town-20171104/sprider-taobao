package com.ylfin.spider.service.impl;

import com.ylfin.spider.config.ShellProperties;
import com.ylfin.spider.register.vo.bean.MailBean;
import com.ylfin.spider.service.ShellService;
import com.ylfin.spider.utils.RemoteShellTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShellServiceImpl  implements ShellService{

//    @Autowired
    ShellProperties shellProperties;

    @Override
    public void register(MailBean mailBean) {
        RemoteShellTool remoteShellTool = new RemoteShellTool(shellProperties.getHost(), shellProperties.getUsername(),
                shellProperties.getPassword(), "utf-8");
        remoteShellTool.exec("/usr/local/sbin/adddovecotuserpasswd   "+mailBean.getUsername()+" "+mailBean.getPassword());
    }
}

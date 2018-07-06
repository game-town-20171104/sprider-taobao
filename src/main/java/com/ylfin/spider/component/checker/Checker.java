package com.ylfin.spider.component.checker;

import com.ylfin.spider.component.BaseSpider;
import com.ylfin.spider.register.enums.RegisterType;
import com.ylfin.spider.register.vo.bean.MailBean;

public interface  Checker {



  String  getCheckCode(MailBean mailBean, RegisterType registerType);


}

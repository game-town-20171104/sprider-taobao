package com.ylfin.spider.Task;

import com.ylfin.spider.register.Register;
import com.ylfin.spider.register.RegisterFactory;
import com.ylfin.spider.register.enums.RegisterType;
import com.ylfin.spider.register.vo.bean.MailBean;
import com.ylfin.spider.vo.SpiderQueue;

public class Mail163Thread extends AbstractThread<MailBean> {


    public Mail163Thread(SpiderQueue<MailBean> queue, RegisterFactory registerFactory) {
        super(queue, registerFactory);
    }

    @Override
    protected Register<MailBean> getRegister() {
        return registerFactory.getRegister(RegisterType.mail163);
    }
}

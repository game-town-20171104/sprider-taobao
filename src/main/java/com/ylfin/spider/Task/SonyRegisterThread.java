package com.ylfin.spider.Task;

import com.ylfin.spider.register.Register;
import com.ylfin.spider.register.RegisterFactory;
import com.ylfin.spider.register.enums.RegisterType;
import com.ylfin.spider.register.vo.bean.SonyBean;
import com.ylfin.spider.vo.SpiderQueue;

public class SonyRegisterThread extends AbstractThread<SonyBean> {

    public SonyRegisterThread(SpiderQueue<SonyBean> queue, RegisterFactory registerFactory) {
        super(queue, registerFactory);
    }

    @Override
    protected Register<SonyBean> getRegister() {
        return registerFactory.getRegister(RegisterType.sony);
    }
}

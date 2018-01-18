package com.ylfin.spider.Task;

import com.ylfin.spider.register.Register;
import com.ylfin.spider.register.RegisterFactory;
import com.ylfin.spider.register.enums.RegisterType;
import com.ylfin.spider.register.vo.bean.NintendoBean;
import com.ylfin.spider.vo.SpiderQueue;

public class NintendoThread extends AbstractThread<NintendoBean> {
    public NintendoThread(SpiderQueue<NintendoBean> queue, RegisterFactory registerFactory) {
        super(queue, registerFactory);
    }

    @Override
    protected Register<NintendoBean> getRegister() {
        return registerFactory.getRegister(RegisterType.nintendo);
    }
}

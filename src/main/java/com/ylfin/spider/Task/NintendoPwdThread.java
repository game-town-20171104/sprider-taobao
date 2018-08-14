package com.ylfin.spider.Task;

import com.ylfin.spider.register.Register;
import com.ylfin.spider.register.RegisterFactory;
import com.ylfin.spider.register.enums.RegisterType;
import com.ylfin.spider.register.vo.NintendoPwdVO;
import com.ylfin.spider.vo.SpiderQueue;

/**
 * @author: godslhand
 * @date: 2018/8/14
 * @description:
 */
public class NintendoPwdThread extends AbstractThread<NintendoPwdVO> {
    public NintendoPwdThread(SpiderQueue<NintendoPwdVO> queue, RegisterFactory registerFactory) {
        super(queue, registerFactory);
    }

    @Override
    protected Register<NintendoPwdVO> getRegister() {
        return registerFactory.getRegister(RegisterType.nintendoPwd);
    }
}

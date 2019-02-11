package com.ylfin.spider.Task;

import com.ylfin.spider.account.vo.bean.CheckMission;
import com.ylfin.spider.register.Register;
import com.ylfin.spider.register.RegisterFactory;
import com.ylfin.spider.register.enums.RegisterType;
import com.ylfin.spider.vo.SpiderQueue;

/**
 * @author: godslhand
 * @date: 2019/2/11
 * @description:
 */
public class CheckMissionTask extends AbstractThread<CheckMission> {
    public CheckMissionTask(SpiderQueue<CheckMission> queue, RegisterFactory registerFactory) {
        super(queue, registerFactory);
    }

    @Override
    protected Register<CheckMission> getRegister() {
        return registerFactory.getRegister(RegisterType.nintendoPwdModify);
    }
}

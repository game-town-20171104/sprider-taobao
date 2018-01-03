package com.ylfin.spider.Task;

import com.ylfin.spider.cateprice.vo.bean.CatePrice;
import com.ylfin.spider.register.Register;
import com.ylfin.spider.register.RegisterFactory;
import com.ylfin.spider.register.enums.RegisterType;
import com.ylfin.spider.vo.SpiderQueue;

public class CatePriceThread extends AbstractThread<CatePrice> {

    public CatePriceThread(SpiderQueue<CatePrice> queue, RegisterFactory registerFactory) {
        super(queue, registerFactory);
    }

    @Override
    protected Register<CatePrice> getRegister() {
        return registerFactory.getRegister(RegisterType.catePrice);
    }
}

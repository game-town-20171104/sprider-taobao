package com.ylfin.spider.Task;

import com.ylfin.spider.register.Register;
import com.ylfin.spider.vo.SpiderQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractThread<T> implements Runnable{

    Logger logger = LoggerFactory.getLogger(AbstractThread.class);
    protected SpiderQueue<T> queue ;

    protected abstract Register<T> getRegister();

    @Override
    public void run() {
        System.out.println(Thread.currentThread() + "==start==" + Thread.activeCount());
        Register<T> spider = this.getRegister();

        spider.initRegister();
        while (true) {
            T vo = queue.get();

            if (vo == null) {
                System.out.println("消费结束，准备退出……");
                break;
            }
            System.out.println("开始消费：" + vo);
            try {
                spider.handle(vo);
            } catch (Exception e) {
                logger.error(vo + "爬虫脚本出错了", e);
            }

        }

        spider.close();
        System.out.println(Thread.currentThread() + "==end==" + Thread.activeCount());
    }
}

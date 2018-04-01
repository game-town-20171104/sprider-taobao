package com.ylfin.spider.Task;

import com.ylfin.spider.register.Register;
import com.ylfin.spider.register.RegisterFactory;
import com.ylfin.spider.vo.SpiderQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractThread<T> implements Runnable{

    Logger logger = LoggerFactory.getLogger(AbstractThread.class);
    protected SpiderQueue<T> queue ;
    protected RegisterFactory registerFactory;

    private int interval =2;//秒 时间间隔

    public AbstractThread(SpiderQueue<T> queue, RegisterFactory registerFactory) {
        this.queue = queue;
        this.registerFactory = registerFactory;
    }

    protected abstract Register<T> getRegister();

    @Override
    public void run() {
        System.out.println(Thread.currentThread() + "==start==" + Thread.activeCount());
        Register<T> spider = this.getRegister();


        Long lastTime = System.currentTimeMillis();
        while (true) {
            T vo = queue.get();
            if (vo == null) {
                System.out.println("消费结束，准备退出……");
                break;
            }
            System.out.println("vo:=========>"+vo);
            spider.initRegister();
            System.out.println("开始消费：" + vo);
            try {
                spider.handle(vo);
            } catch (Exception e) {
                logger.error(vo + "爬虫脚本出错了", e);
            }

            //超时设置，防止一直失败、短间隔循环
            Long currentTime = System.currentTimeMillis();
            if((currentTime-lastTime)<1000){
                try {
                    Thread.sleep(1000*interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lastTime = currentTime;
            }

            spider.close();

        }


        System.out.println(Thread.currentThread() + "==end==" + Thread.activeCount());
    }


}

package com.ylfin.spider.Task;

import com.ylfin.spider.register.Register;
import com.ylfin.spider.vo.SpiderQueue;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReOpenThread<T> implements Runnable {
    protected SpiderQueue<T> queue ;
    protected Register register;

    private int interval =2;//秒 时间间隔

    public ReOpenThread(SpiderQueue<T> queue, Register<T> register) {
        this.queue = queue;
        this.register = register;
    }


    @Override
    public void run() {
        System.out.println(Thread.currentThread() + "==start==" + Thread.activeCount());
        Long lastTime = System.currentTimeMillis();
        while (true) {
            T vo = queue.get();
            if (vo == null) {
                System.out.println("消费结束，准备退出……");
                break;
            }
            System.out.println("vo:=========>"+vo);
            register.initRegister();
            System.out.println("开始消费：" + vo);
            try {
                register.handle(vo);
            } catch (Exception e) {
                log.error(vo + "爬虫脚本出错了", e);
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

            register.close();

        }


        System.out.println(Thread.currentThread() + "==end==" + Thread.activeCount());
    }
}

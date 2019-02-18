package com.ylfin.spider.controller;

import com.ylfin.spider.Task.SpiderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: godslhand
 * @date: 2019/2/18
 * @description:
 */
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    SpiderTask spiderTask;

    @GetMapping("/submit")
    public Object submitMission(@RequestParam  Integer model) throws Exception {

       if(spiderTask.es.getActiveCount()>0){
           return "有任务在进行中，请稍后再试";
       }
       spiderTask.setModel(model);
       spiderTask.run(null);
       return "任务提交成功";
    }

}

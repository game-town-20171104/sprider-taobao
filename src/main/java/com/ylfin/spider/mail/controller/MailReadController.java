package com.ylfin.spider.mail.controller;

import com.ylfin.spider.mail.service.MailReader;
import com.ylfin.spider.mail.vo.MailContent;
import com.ylfin.spider.mail.vo.MailQueryVo;
import com.ylfin.spider.register.service.MailService;
import com.ylfin.spider.register.vo.bean.MailBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: godslhand
 * @date: 2019/2/18
 * @description:
 */
@RestController
@RequestMapping("/mail")
public class MailReadController {

    @Autowired
    MailService mailService;

    @PostMapping("/query")
    public List<MailContent> query(@Valid @RequestBody MailQueryVo queryVo){
        MailBean mailBean = mailService.findByEmail(queryVo.getEmail());
        if(mailBean ==null){
            throw new RuntimeException("没有找到对应的邮箱账号");
        }
        List<String> keywords = new ArrayList<>();
        String keyword = queryVo.getKeywords();
        keywords = Arrays.asList(StringUtils.split(keyword,","));
        if(StringUtils.isBlank(keyword)){
            return MailReader.searchMail(mailBean,queryVo);
        }
        return MailReader.searchMail(mailBean,keywords);
    }
}

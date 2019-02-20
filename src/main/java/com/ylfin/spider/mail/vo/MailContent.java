package com.ylfin.spider.mail.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author: godslhand
 * @date: 2019/2/17
 * @description:
 */
@Data
public class MailContent {
    private String subject;
    private String sender;
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendDate;
}

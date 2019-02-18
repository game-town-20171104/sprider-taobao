package com.ylfin.spider.mail.vo;

import lombok.Data;

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
    private Date sendDate;
}

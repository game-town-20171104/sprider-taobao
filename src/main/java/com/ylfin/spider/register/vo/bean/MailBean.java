package com.ylfin.spider.register.vo.bean;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("mail")
public class MailBean {
    private Long id;
    private String username ;
    private String email;
    private String password;
    private String phone;
}

package com.ylfin.spider.register.vo.bean;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("reg_mail")
public class MailBean {
    @TableId
    private Long id;
    private String username ;
    private String email;
    private String password;
    private String phone;
    private Boolean success;
}

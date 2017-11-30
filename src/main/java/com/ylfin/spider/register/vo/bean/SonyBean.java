package com.ylfin.spider.register.vo.bean;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("reg_sony")
public class SonyBean {
    private Long id;
    private String psn;
    private String username ;
    private String password;
    private String question;
    private String quesAnswer;
    private String country;
    private String province ;
    private String phone;
    private Boolean success;
    private Boolean active;

}

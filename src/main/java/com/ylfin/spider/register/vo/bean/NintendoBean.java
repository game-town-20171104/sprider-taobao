package com.ylfin.spider.register.vo.bean;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("reg_nintendo")
public class NintendoBean {
    @TableId
    private Long id;
    private String nickname;
    private String email;
    private String password;

    private Boolean success;
    private Boolean active;

}

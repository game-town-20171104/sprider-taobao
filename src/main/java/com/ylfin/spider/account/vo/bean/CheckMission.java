package com.ylfin.spider.account.vo.bean;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * @author: godslhand
 * @date: 2019/2/11
 * @description:
 */
@Data
@TableName("check_mission")
public class CheckMission {
    public static String tag_pwd_modify = "PWD";
    public static String tag_account_return = "BACK";

    @TableId
    private Long id;
    private String tag ;
    private String account ;
    private String password;
    private Integer type;
    private Integer strategy;
    private String newPassword;
    private Integer result;
    private Boolean bright;
    private String error;
    private Boolean  active;


}

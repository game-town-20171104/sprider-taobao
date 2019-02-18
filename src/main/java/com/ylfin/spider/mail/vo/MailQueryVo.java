package com.ylfin.spider.mail.vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author: godslhand
 * @date: 2019/2/18
 * @description:
 */
@Data
public class MailQueryVo  extends MailContent{
    @NotBlank(message = "目标邮箱不能为空")
    private String email ;
    private String keywords ;
}

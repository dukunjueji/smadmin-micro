package com.ucar.smadmin.base.bd.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 版权说明：Copyright (c) 2018 ucarinc. All Rights Reserved.
 *
 * @author：shixian.zhang@ucarinc.com
 * @version：v1.0
 * @date: 2018/10/24
 * 说明：修改密码发送验证码参数
 */
public class SendCodeVO implements Serializable {
    private static final long serialVersionUID = -5766666465966750526L;

    /**
     * 旧密码
     */
    @NotBlank(message = "旧密码不能为空")
    @Length(min = 6, max = 10, message = "密码长度须在6-10位之间")
    private String oldpassword;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @Length(min = 6, max = 10, message = "密码长度须在6-10位之间")
    private String newpassword;

    /**
     *确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    @Length(min = 6, max = 10, message = "密码长度须在6-10位之间")
    private String confirmpassword;

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }

    @Override
    public String toString() {
        return "SendCodeVO{" +
                "oldpassword='" + oldpassword + '\'' +
                ", newpassword='" + newpassword + '\'' +
                ", confirmpassword='" + confirmpassword + '\'' +
                '}';
    }
}

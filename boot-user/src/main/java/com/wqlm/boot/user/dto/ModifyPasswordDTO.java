package com.wqlm.boot.user.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ModifyPasswordDTO {

    /**
     * 旧密码
     */
    @Length(min = 8, max = 20, message = "旧密码必须是8至20位")
    private String oldPassword;

    /**
     * 新密码
     */
    @Length(min = 8, max = 20, message = "新密码必须是8至20位")
    private String newPassword;
}

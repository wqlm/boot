package com.wqlm.boot.user.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class RegisterDTO implements Serializable {

    private static final long serialVersionUID = 7847513868092830014L;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String userName;

    /**
     * 密码
     */
    @Length(min =8, max =20, message = "密码必须是8至20位")
    private String password;

}

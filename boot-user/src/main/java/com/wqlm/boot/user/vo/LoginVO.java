package com.wqlm.boot.user.vo;

import lombok.Data;

/**
 * 登陆成功后返回的用户信息和token
 */
@Data
public class LoginVO {
    /**
     * 用户名
     */
    private String userName;

    /**
     * token
     */
    private String token;
}

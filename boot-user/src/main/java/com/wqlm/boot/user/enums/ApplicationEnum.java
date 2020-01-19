package com.wqlm.boot.user.enums;

import lombok.Getter;

/**
 * 业务结果状态码
 */
@Getter
public enum ApplicationEnum {

    SUCCESS("2000","业务执行成功"),



    PARAMETER_BIND_FAIL("4000","参数绑定失败"),
    PARAMETER_VERIFY_FAIL("4001","参数校验失败"),

    USER_NAME_REPETITION("4101","用户名已存在"),
    USER_OR_PWD_ERR("4102","用户名或密码错误"),
    NO_LOGIN("4103","未登陆"),
    YET_LOGIN("4104","已登陆"),
    USER_NO_EXIST("4105","用户不存在"),
    PASSWORD_ERR("4106","密码错误"),

    REQUEST_FREQUENTLY("4201","请求太过频繁"),

    TOKEN_INVALID("4301", "token失效"),


    FAIL("5000","业务执行失败");


    /**
     * 设计原则
     * 2xxx 业务执行成功
     * 4xxx 由于用户导致的错误，比如注册时，重复注册导致的注册失败
     * 5xxx 由于系统原因导致的错误
     */
    private String code;

    private String message;

    ApplicationEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

}

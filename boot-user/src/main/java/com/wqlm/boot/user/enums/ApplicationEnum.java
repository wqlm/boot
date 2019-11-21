package com.wqlm.boot.user.enums;

import lombok.Getter;


@Getter
public enum ApplicationEnum {

    SUCCESS("2000","业务执行成功"),
    FAIL("5000","业务执行失败"),
    USER_NAME_REPETITION("4001","用户名已存在"),
    USER_OR_PWD_ERR("4002","用户名或密码错误"),
    NO_LOGIN("4003","未登陆"),
    YET_LOGIN("4004","已登陆"),
    REQUEST_FREQUENTLY("4005","请求太过频繁");


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

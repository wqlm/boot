package com.wqlm.boot.user.exception;

import com.wqlm.boot.user.enums.ApplicationEnum;


public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = -1629701124990906510L;

    /**
     * 每一个抛出的异常都必须对应一个 ApplicationEnum
     */
    private ApplicationEnum applicationEnum;

    public ApplicationEnum getApplicationEnum() {
        return applicationEnum;
    }

    public ApplicationException(ApplicationEnum applicationEnum) {
        super(applicationEnum.getCode() + " : " + applicationEnum.getMessage());
        this.applicationEnum = applicationEnum;
    }

}

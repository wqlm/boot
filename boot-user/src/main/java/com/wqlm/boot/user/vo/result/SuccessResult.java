package com.wqlm.boot.user.vo.result;


import com.wqlm.boot.user.enums.ApplicationEnum;

public class SuccessResult<T> extends Result<T> {

    private static final long serialVersionUID = 3388694251281043597L;

    public SuccessResult() {
        setStatus(true);
        setCode(ApplicationEnum.SUCCESS.getCode());
        setMsg(ApplicationEnum.SUCCESS.getMessage());
    }
    
    public SuccessResult(T data) {
        setStatus(true);
        setCode(ApplicationEnum.SUCCESS.getCode());
        setMsg(ApplicationEnum.SUCCESS.getMessage());
        setData(data);
    }

}

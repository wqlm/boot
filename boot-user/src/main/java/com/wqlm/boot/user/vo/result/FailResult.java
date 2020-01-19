package com.wqlm.boot.user.vo.result;


import com.wqlm.boot.user.enums.ApplicationEnum;

public class FailResult<T> extends Result<T> {

    private static final long serialVersionUID = -6073157176763840816L;

    public FailResult() {
        setStatus(false);
        setCode(ApplicationEnum.FAIL.getCode());
        setMsg(ApplicationEnum.FAIL.getMessage());
    }
    
    public FailResult(ApplicationEnum applicationEnum) {
        setStatus(false);
        setCode(applicationEnum.getCode());
        setMsg(applicationEnum.getMessage());
    }


    public FailResult(ApplicationEnum applicationEnum,  T data) {
        setStatus(false);
        setCode(applicationEnum.getCode());
        setMsg(applicationEnum.getMessage());
        setData(data);
    }



}

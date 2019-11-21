package com.wqlm.boot.user.vo.result;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;


public class Result<T> implements Serializable {

    private static final long serialVersionUID = 3149361523750900688L;

    /**
     * true:业务执行成功， false:业务执行失败
     */
    private Boolean status;

    /**
     * 状态码
     */
    private String code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 结果数据
     */
    private T data;


    public Boolean getStatus() {
        return status;
    }

    void setStatus(Boolean status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        String jsonData = null;
        try {
            jsonData = new ObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "Result{" + "status=" + status + ", code='" + code + '\''
                + ", msg='" + msg + '\'' + ", data=" + jsonData + '}';
    }
}

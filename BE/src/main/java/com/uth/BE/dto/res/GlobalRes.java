package com.uth.BE.dto.res;


import org.springframework.http.HttpStatus;

public class GlobalRes <T>{
    private int code;
    private String msg;
    private T data;

    public GlobalRes(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public GlobalRes(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public GlobalRes(HttpStatus httpStatus, String msg, T data) {
        this.code = httpStatus.value();
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

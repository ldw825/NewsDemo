package com.kent.newspaper.module.newsbrowse.entity;

/**
 * @author Kent
 * @version 1.0
 * @date 2018/12/23
 */
public abstract class ResponseData<T> {
    private String status;
    private String msg;
    private T result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}

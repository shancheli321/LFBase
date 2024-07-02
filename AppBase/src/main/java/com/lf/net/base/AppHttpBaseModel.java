package com.lf.net.base;

import java.io.Serializable;

/**
 * File descripition:   mode基类，根据实际情况自定义
 *
 * @author 宋宁
 * @date 2020/8/20
 */
public class AppHttpBaseModel<T> implements Serializable {

    private String message;

    private int code;

    private T data;

    public AppHttpBaseModel(int error_code, String reason) {

        this.message = reason;
        this.code = error_code;
    }

    public AppHttpBaseModel() {

    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public int getCode() {

        return code;
    }

    public void setCode(int code) {

        this.code = code;
    }

    public T getData() {

        return data;
    }

    public void setData(T data) {

        this.data = data;
    }
}

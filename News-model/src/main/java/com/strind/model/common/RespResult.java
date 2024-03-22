package com.strind.model.common;

import com.strind.model.common.enums.AppHttpCodeEnum;

import java.io.Serializable;

/**
 * @author strind
 * @version 1.0
 * @description 通用结果返回类
 * @date 2024/3/20 22:19
 */
public class RespResult<T> implements Serializable {

    private Integer code;

    private String errorMessage;

    private T data;

    public RespResult() {
        this.code = 200;
    }

    public RespResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public RespResult(Integer code, String errorMessage, T data) {
        this.code = code;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public RespResult(String errorMessage, T data) {
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public static RespResult errorResult(int code, String msg) {
        RespResult result = new RespResult();
        return result.error(code, msg);
    }

    public static RespResult okResult(int code, String msg) {
        RespResult result = new RespResult();
        return result.ok(code, null, msg);
    }

    public static RespResult okResult(Object data) {
        RespResult result = setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS, AppHttpCodeEnum.SUCCESS.getErrorMessage());
        if(data!=null) {
            result.setData(data);
        }
        return result;
    }

    public static RespResult errorResult(AppHttpCodeEnum enums){
        return setAppHttpCodeEnum(enums,enums.getErrorMessage());
    }

    public static RespResult errorResult(AppHttpCodeEnum enums, String errorMessage){
        return setAppHttpCodeEnum(enums,errorMessage);
    }

    public static RespResult setAppHttpCodeEnum(AppHttpCodeEnum enums){
        return okResult(enums.getCode(),enums.getErrorMessage());
    }

    private static RespResult setAppHttpCodeEnum(AppHttpCodeEnum enums, String errorMessage){
        return okResult(enums.getCode(),errorMessage);
    }

    public RespResult<?> error(Integer code, String msg) {
        this.code = code;
        this.errorMessage = msg;
        return this;
    }

    public RespResult<?> ok(Integer code, T data) {
        this.code = code;
        this.data = data;
        return this;
    }

    public RespResult<?> ok(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.errorMessage = msg;
        return this;
    }

    public RespResult<?> ok(T data) {
        this.data = data;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

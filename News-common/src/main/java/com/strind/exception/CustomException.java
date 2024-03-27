package com.strind.exception;

import com.strind.model.common.enums.AppHttpCodeEnum;

/**
 * @author strind
 * @version 1.0
 * @description 同一异常结果返回
 * @date 2024/3/23 20:57
 */
public class CustomException extends RuntimeException {

    private AppHttpCodeEnum appHttpCodeEnum;

    public CustomException(AppHttpCodeEnum appHttpCodeEnum){
        this.appHttpCodeEnum = appHttpCodeEnum;
    }

    public AppHttpCodeEnum getAppHttpCodeEnum() {
        return appHttpCodeEnum;
    }

}

package com.yd.telescopeapi.exception;

import com.yd.telescopeapi.enums.ResultEnums;

/**
 * 自定义异常
 *
 * @author zygong
 * @create 2017-12-22 12:55
 **/
public class MyException extends RuntimeException{

    /**
     * 错误编码
     */
    private Integer code;

    public MyException(ResultEnums enums){
        super(enums.getMsg());
        this.code = enums.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}

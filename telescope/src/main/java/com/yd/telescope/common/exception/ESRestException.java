package com.yd.telescope.common.exception;

import com.yd.telescope.common.enums.ResultEnums;

public class ESRestException extends Exception {
    private int code;

    public ESRestException(ResultEnums error) {
        super(error.getMsg());
        this.code = error.getCode();
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

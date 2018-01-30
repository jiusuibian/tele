package com.yd.telescope.common.exception;

import com.yd.telescope.common.enums.ResultEnums;

public class EncryException extends Exception {
    private int code;

    public EncryException(ResultEnums error) {
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

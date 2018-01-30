package com.yd.telescope.common.enums;

public enum ResultEnums {
	UNKNOWN_ERROR(-1, "系统错误"),
	SUCCESS(0, "成功"),
	OTHER_ERROR(100, "其他错误"),
	NULL_ERROR(101, "数据为空"),
	VERIFY_ERROR(102, "验证码错误"),
	USER_ERROR(103, "用户名或者密码错误"),
	DATABASE_OPERATION_ERROR(104, "数据库操作错误"),
	EXIST(105, "数据已存在"),
	NOT_EXIST(106, "数据不存在"),
	PARAMETER_ERROR(107, "请求参数错误"),
	MD5_ERROR(108, "MD5加密错误"),

    ES_ERROR(201, "请求失败");

	private int code;
	private String msg;

	ResultEnums(int code, String msg) {
		this.code = code;
		this.msg = msg;
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
}

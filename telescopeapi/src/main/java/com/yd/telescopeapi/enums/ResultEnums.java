package com.yd.telescopeapi.enums;

/**
 *    错误信息
 *
 * @author zygong
 * @date 2017/12/22 13:02
 */
public enum ResultEnums {
	UNKONW_ERROR(-1, "未知错误！"),
	SUCCESS(0, "成功!"),
	HEADER_ERROR(100, "头信息错误！"),
	VIEW_INFO_NULL(101, "浏览信息为空！"),
	CHECK_POST_ERROR(201, "请求错误！");
	
	private Integer code;
	
	private String msg;

	private ResultEnums(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}

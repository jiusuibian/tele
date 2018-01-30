package com.yd.telescopeapi.util;

import java.util.List;
import java.util.UUID;

import com.yd.telescopeapi.domain.Result;
import com.yd.telescopeapi.enums.ResultEnums;

public class ResultUtils {

	public static Result<Object> success(Object obj) {
		Result<Object> result = new Result<Object>();
		result.setCode(0);
		result.setMsg("成功！");
		result.setData(obj);
		result.setList(null);
		return result;
	}
	
	public static Result<Object> success(List<Object> list) {
		Result<Object> result = new Result<Object>();
		result.setCode(0);
		result.setMsg("成功！");
		result.setData(null);
		result.setList(list);
		return result;
	}
	
	public static Result success() {
		Result<Object> result = new Result<Object>();
		result.setCode(0);
		result.setMsg("成功！");
		return result;
	}
	
	public static Result error(ResultEnums enums) {
		Result result = new Result();
		result.setCode(enums.getCode());
		result.setMsg(enums.getMsg());
		return result;
	}
	
	public static Result error(Integer code, String msg) {
		Result result = new Result();
		result.setCode(-1);
		result.setMsg(msg);
		return result;
	}

}

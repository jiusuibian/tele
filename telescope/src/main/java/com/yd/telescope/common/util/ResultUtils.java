package com.yd.telescope.common.util;


import com.yd.telescope.common.dto.JsonResult;
import com.yd.telescope.common.enums.ResultEnums;

public class ResultUtils {

	public static JsonResult result(ResultEnums enums) {
		JsonResult result = new JsonResult();
		result.setCode(enums.getCode());
		result.setMessage(enums.getMsg());
		return result;
	}
	
	public static JsonResult error(int code, String msg) {
		JsonResult result = new JsonResult();
		result.setCode(code);
		result.setMessage(msg);
		return result;
	}

	public static JsonResult success(Object obj){
		JsonResult result = new JsonResult();
		result.setCode(ResultEnums.SUCCESS.getCode());
		result.setMessage(ResultEnums.SUCCESS.getMsg());
		result.setData(obj);
		return result;
	}
}

package com.yd.telescopeapi.handler;

import com.yd.telescopeapi.domain.Result;
import com.yd.telescopeapi.exception.MyException;
import com.yd.telescopeapi.util.ResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *    拦截异常统一处理
 *
 * @author zygong
 * @date 2017/12/22 12:59
 */
@ControllerAdvice
public class ExceptionHandle {
	private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

	@ExceptionHandler(value=Exception.class)
	@ResponseBody
	public Result<Object> handle(Exception e) {
		if(e instanceof MyException) {
			MyException girlException = (MyException) e;
			return ResultUtils.error(girlException.getCode(), girlException.getMessage());
		}else {
			e.printStackTrace();
			logger.error("【系统异常】:{}", e.getMessage());
			return ResultUtils.error(-1, "未知错误！");
		}
	}
}

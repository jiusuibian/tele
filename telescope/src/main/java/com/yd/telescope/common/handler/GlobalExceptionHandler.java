package com.yd.telescope.common.handler;

import com.alibaba.druid.support.json.JSONUtils;
import com.yd.telescope.common.dto.JsonResult;
import com.yd.telescope.common.enums.ResultEnums;
import com.yd.telescope.common.exception.ESRestException;
import com.yd.telescope.common.exception.RepositoryException;
import com.yd.telescope.common.exception.ServiceException;
import com.yd.telescope.common.util.ResultUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@ControllerAdvice
public class GlobalExceptionHandler {

    //采用spring-boot 默认的映射/error
    public static final String DEFAULT_ERROR_VIEW = "/error";
//    public static final String DEFAULT_ERROR_VIEW = "errorPage/error";

    /**
     *
     * 捕获@RequestMapping注解的方法抛出的Exception异常并处理：
     * 若是ajax请求或请求端接受json数据则返回json信息；否则转发（forward）到默认的/error映射，error.html页面展示信息,因为是forward所以不用再经过拦截器处理。
     * 若是去掉该方法或者去掉注解@ExceptionHandler，则spring-boot对异常的处理：302重定向到到默认的错误异常处理映射/error,因为是302重定向，
     * 所以会经过拦截器处理。
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, HttpServletResponse res, Exception e) throws Exception {

        e.printStackTrace();//打印测试
        String accept = req.getHeader("Accept");

        String requestType = req.getHeader("X-Requested-With");
        boolean ajax = (requestType != null && requestType.equalsIgnoreCase("XMLHttpRequest")) ? true : false;
        if(ajax || accept.contains("json")) {     //ajax请求或者请求端接受json数据

            PrintWriter writer = res.getWriter();
            writer.write(JSONUtils.toJSONString( ResultUtils.error(500,e.getMessage())));
            writer.flush();
            writer.close();
            return null;
        } else {
            ModelAndView mav = new ModelAndView();
            mav.addObject("exception", e);
//            mav.addObject("url", req.getRequestURL());
            mav.setViewName(DEFAULT_ERROR_VIEW);
            return mav;
        }
    }


    /**
     * 会优先处理JsonException异常
     * 返回json格式
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = {RepositoryException.class, ServiceException.class})
    @ResponseBody
    public JsonResult jsonErrorHandler(Exception e){
        if(e instanceof RepositoryException){
            return ResultUtils.error(((RepositoryException)e).getCode(), e.getMessage());
        }else if(e instanceof ServiceException){
            return ResultUtils.error(((ServiceException)e).getCode(), e.getMessage());
        }else if(e instanceof ESRestException){
            return ResultUtils.error(((ESRestException)e).getCode(), e.getMessage());
        }else {
            return ResultUtils.result(ResultEnums.UNKNOWN_ERROR);
        }
    }

}

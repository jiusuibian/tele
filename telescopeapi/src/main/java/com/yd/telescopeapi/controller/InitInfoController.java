package com.yd.telescopeapi.controller;

import com.yd.telescopeapi.domain.Initinfo;
import com.yd.telescopeapi.domain.Result;
import com.yd.telescopeapi.enums.ResultEnums;
import com.yd.telescopeapi.repository.InitinfoRepository;
import com.yd.telescopeapi.service.InitinfoService;
import com.yd.telescopeapi.util.DateEditor;
import com.yd.telescopeapi.util.ResultUtils;
import jdk.nashorn.internal.ir.IdentNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.zip.Inflater;

/**  
 *   手机信息初始化接口
 *   
 * @author zygong
 * @date 2017/12/22 11:40
 */ 
@RestController
public class InitInfoController {

    @Autowired
    private InitinfoService initinfoService;

    /**
     *    手机信息初始化接口
     *
     * @author zygong
     * @date 2017/12/22 12:52
     * @param []
     * @return java.lang.String
     */
    @PostMapping("/initInfo")
    public Result initInfo(@RequestBody Initinfo initinfo, @RequestHeader(value = "t_app_id")String appId){
        if(StringUtils.isBlank(appId)){
            return ResultUtils.error(ResultEnums.HEADER_ERROR);
        }
        initinfo.setT_app_id(appId);
        initinfoService.saveAndFlush(initinfo);
        return ResultUtils.success();
    }

}
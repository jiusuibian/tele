package com.yd.telescopeapi.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yd.telescopeapi.domain.CallInfo;
import com.yd.telescopeapi.domain.Initinfo;
import com.yd.telescopeapi.domain.Result;
import com.yd.telescopeapi.domain.ViewInfo;
import com.yd.telescopeapi.enums.ResultEnums;
import com.yd.telescopeapi.repository.InitinfoRepository;
import com.yd.telescopeapi.service.InitinfoService;
import com.yd.telescopeapi.util.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户端接口调用
 *
 * @author zygong
 * @create 2017-12-25 10:34
 **/
@RestController
public class CallInfoController {

    @Autowired
    private InitinfoService initinfoService;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    private Gson gson = new GsonBuilder().create();

    @PostMapping("callInfo")
    public Result callInfo(@RequestBody CallInfo callInfo, @RequestHeader(value = "t_app_id")String appId){
        if(StringUtils.isBlank(appId)){
            return ResultUtils.error(ResultEnums.HEADER_ERROR);
        }
        callInfo.setT_app_id(appId);
        // 通过设备号和appid获取手机信息
        Initinfo initInfo = initinfoService.getByDevIdAndAppId(callInfo.getT_dev_id(), appId);

        callInfo.setInitinfo(initInfo);
        callInfo.setT_infoType("callInfo");

        //  将消息发送到kafka
        kafkaTemplate.send("callInfo", gson.toJson(callInfo));

        return ResultUtils.success();
    }
}

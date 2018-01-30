package com.yd.telescopeapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yd.telescopeapi.domain.BugInfo;
import com.yd.telescopeapi.domain.Initinfo;
import com.yd.telescopeapi.domain.Result;
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

import java.util.Random;

/**
 * 崩溃接口
 *
 * @author zygong
 * @create 2017-12-25 9:44
 **/
@RestController
public class BugInfoController {

    @Autowired
    private InitinfoService initinfoService;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @PostMapping("/bugInfo")
    public Result bugInfo(@RequestBody BugInfo bugInfo, @RequestHeader(value = "appId")String appId) throws JsonProcessingException {
        if(StringUtils.isBlank(appId)){
            return ResultUtils.error(ResultEnums.HEADER_ERROR);
        }
        bugInfo.setT_app_id(appId);
        // 通过设备号和appid获取手机信息
        Initinfo initinfo = initinfoService.getByDevIdAndAppId(bugInfo.getT_dev_id(), appId);

        bugInfo.setT_status(new Random().nextInt(4));
        bugInfo.setInitInfo(initinfo);
        bugInfo.setT_infoType("buginfo");
        bugInfo.setT_app_id("m" + bugInfo.getT_app_id());

        //  将消息发送到kafka
        ObjectMapper mapper = new ObjectMapper();
        kafkaTemplate.send("bugInfo", mapper.writeValueAsString(bugInfo));
        return ResultUtils.success();
    }
}

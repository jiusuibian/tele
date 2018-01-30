package com.yd.telescopeapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;

/**
 * 页面操作接口
 *
 * @author zygong
 * @create 2017-12-25 10:15
 **/
@RestController
public class ViewInfoController {

    @Autowired
    private InitinfoService initinfoService;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @PostMapping("viewInfo")
    public Result viewInfo(@RequestBody List<ViewInfo> viewInfoList, @RequestHeader(value = "t_app_id")String appId) throws JsonProcessingException {
        if(StringUtils.isBlank(appId)){
            return ResultUtils.error(ResultEnums.HEADER_ERROR);
        }else if(viewInfoList == null || viewInfoList.isEmpty()){
            return ResultUtils.error(ResultEnums.VIEW_INFO_NULL);
        }
        for(ViewInfo viewInfo : viewInfoList){
            viewInfo.setT_app_id(appId);
            // 通过设备号和appid获取手机信息
            Initinfo initInfo = initinfoService.getByDevIdAndAppId(viewInfo.getT_dev_id(), appId);

            viewInfo.setInitInfo(initInfo);
            viewInfo.setT_infoType("viewinfo");
            viewInfo.setT_app_id("m" + viewInfo.getT_app_id());

            //  将消息发送到kafka
            ObjectMapper mapper = new ObjectMapper();
            kafkaTemplate.send("viewInfo", mapper.writeValueAsString(viewInfo));
//            System.out.println(mapper.writeValueAsString(viewInfo));
        }

        return ResultUtils.success();
    }
}

package com.yd.telescopeapi.service.impl;

import com.yd.telescopeapi.domain.Initinfo;
import com.yd.telescopeapi.repository.InitinfoRepository;
import com.yd.telescopeapi.service.InitinfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class InitinfoServiceImpl implements InitinfoService {

    @Autowired
    private InitinfoRepository initinfoRepository;

    @Override
    public Initinfo getByDevIdAndAppId(String tDevId, String appId) {
        Initinfo initinfo = null;
        if(!StringUtils.isAnyBlank(tDevId, appId)){
            initinfo = initinfoRepository.getByDevIdAndAppId(tDevId, appId);
        }
        return initinfo;
    }

    @Override
    public void saveAndFlush(Initinfo initinfo) {
        // 首先查询设备是否存在
        Initinfo oldInitInfo = initinfoRepository.getByDevIdAndAppId(initinfo.getT_dev_id(), initinfo.getT_app_id());
        if(oldInitInfo == null){
            initinfo.setT_up_time(new Date());
            initinfo.setT_last_time(new Date());
            initinfoRepository.saveAndFlush(initinfo);
        }else{
            initinfo.setId(oldInitInfo.getId());
            initinfo.setT_last_time(new Date());
            initinfoRepository.saveAndFlush(initinfo);
        }
    }
}

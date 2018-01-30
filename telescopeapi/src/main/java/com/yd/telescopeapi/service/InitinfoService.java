package com.yd.telescopeapi.service;

import com.yd.telescopeapi.domain.Initinfo;

public interface InitinfoService {

    Initinfo getByDevIdAndAppId(String tDevId, String appId);

    void saveAndFlush(Initinfo initinfo);
}

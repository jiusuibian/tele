package com.yd.telescopeapi.service.impl;

import com.yd.telescopeapi.domain.App;
import com.yd.telescopeapi.repository.AppRepository;
import com.yd.telescopeapi.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private AppRepository appRepository;

    @Override
    @Cacheable(value = "appStatus")
    public Map<String, String> getAppStatus() {
        Map<String, String> map = new HashMap<>();
        List<App> all = appRepository.findAll();
        if(all != null && !all.isEmpty()){
            for(App app : all){
                map.put(app.getAppid(), app.getStatus());
            }
        }
        return map;
    }

    @Override
    @Cacheable(value = "appKey")
    public Map<String, String> getAppKey() {
        Map<String, String> map = new HashMap<>();
        List<App> all = appRepository.findAll();
        if(all != null && !all.isEmpty()){
            for(App app : all){
                map.put(app.getAppid(), app.getAppkey());
            }
        }
        return map;
    }

    @CacheEvict(value = "appStatus", allEntries = true)
    @Scheduled(fixedDelay = 1000 * 60 *60)
    public void deleteAppStatusCache(){
    }

    @CacheEvict(value = "appKey", allEntries = true)
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
    public void deleteAppKeyCache(){
    }
}

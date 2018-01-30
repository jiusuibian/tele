package com.yd.telescope.management.service;

import com.yd.telescope.common.exception.ServiceException;
import com.yd.telescope.management.domain.App;

import java.util.List;

public interface AppService {

    boolean save(App app)throws ServiceException;
    void update(App app) throws ServiceException;
    void delete(String appid) throws ServiceException;
    boolean exists(String appid) throws ServiceException;
    App findByAppid(String appid) throws ServiceException;
    List<App> findByApptypeAndUsername(String apptype, String username) throws ServiceException;

    boolean exists(String username, String appname, String ostype)throws ServiceException;
}

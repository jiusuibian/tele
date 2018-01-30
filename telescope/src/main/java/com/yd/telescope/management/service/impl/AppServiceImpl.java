package com.yd.telescope.management.service.impl;

import com.yd.telescope.common.enums.ResultEnums;
import com.yd.telescope.common.exception.ServiceException;
import com.yd.telescope.management.domain.App;
import com.yd.telescope.management.repository.AppRepository;
import com.yd.telescope.management.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private AppRepository appRepository;

    @Override
    public boolean save(App app) throws ServiceException {
        try {
            return null != appRepository.saveAndFlush(app);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public void update(App app) throws ServiceException {
        try {
            appRepository.update(app.getAppid(),app.getStatus(),app.getModify_time(),app.getModifier());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public void delete(String appid) throws ServiceException {
        try {
            appRepository.delete(appid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public boolean exists(String username, String appname, String ostype) throws ServiceException {
        try {
            return null != appRepository.exists(username,appname,ostype);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public boolean exists(String appid) throws ServiceException {
        try {
            return appRepository.exists(appid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public App findByAppid(String appid) throws ServiceException {

        try {
            return appRepository.findOne(appid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public List<App> findByApptypeAndUsername(String apptype, String username) throws ServiceException {
        try {
            return appRepository.findByApptypeAndUsername(apptype,username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

}

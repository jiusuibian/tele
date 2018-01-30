package com.yd.telescope.system.service;

import com.yd.telescope.common.dto.DatatableRes;
import com.yd.telescope.common.exception.ServiceException;
import com.yd.telescope.system.domain.User;
import com.yd.telescope.system.domain.UserCondition;

import java.util.List;

public interface UserService {
    DatatableRes<List<User>> findAllByPaging(UserCondition condition) throws ServiceException;
    boolean save(User user) throws ServiceException;
    void delete(String username) throws ServiceException;
    boolean exists(String username) throws ServiceException;
    void update(User user) throws ServiceException;
    User findByUsername(String username) throws ServiceException;

}

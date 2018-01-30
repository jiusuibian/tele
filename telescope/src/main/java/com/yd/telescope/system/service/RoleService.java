package com.yd.telescope.system.service;

import com.yd.telescope.common.dto.DatatableRes;
import com.yd.telescope.common.exception.ServiceException;
import com.yd.telescope.system.domain.Role;
import com.yd.telescope.system.domain.RoleCondition;

import java.util.List;
import java.util.Set;

public interface RoleService {
    List<Role> findAll() throws ServiceException;
    Set<Role> findByRole_ids(List<String> role_ids) throws ServiceException;
    DatatableRes<List<Role>> findAllByPaging(RoleCondition condition) throws ServiceException;
    boolean exists(String role_id) throws ServiceException;
    boolean save(Role role) throws ServiceException;
    void delete(String role_id) throws ServiceException;
    Role findByRole_id(String role_id) throws ServiceException;
    void update(Role role) throws ServiceException;
}

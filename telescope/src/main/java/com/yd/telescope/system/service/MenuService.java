package com.yd.telescope.system.service;

import com.yd.telescope.common.exception.ServiceException;
import com.yd.telescope.system.domain.Menu;
import com.yd.telescope.system.domain.MenuCondition;
import com.yd.telescope.system.domain.TreeMenu;

import java.util.List;
import java.util.Set;

public interface MenuService {
    List<Menu> findAllByPaging(MenuCondition condition) throws ServiceException;
    List<Menu> findAll() throws ServiceException;
    boolean save(Menu menu) throws ServiceException;
    void delete(long menuId) throws ServiceException;
    boolean exists(long menuId) throws ServiceException;
    Menu findByMenuid(long menuId) throws ServiceException;
    void update(Menu menu) throws ServiceException;
    Set<Menu> findByMenu_ids(List<Integer> menu_ids) throws ServiceException;
    List<TreeMenu> findByParent_id(long parent_id) throws ServiceException;
}

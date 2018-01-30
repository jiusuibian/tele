package com.yd.telescope.system.service.impl;

import com.yd.telescope.common.enums.ResultEnums;
import com.yd.telescope.common.exception.ServiceException;
import com.yd.telescope.system.domain.Menu;
import com.yd.telescope.system.domain.MenuCondition;
import com.yd.telescope.system.domain.TreeMenu;
import com.yd.telescope.system.repository.MenuRepository;
import com.yd.telescope.system.repository.TreeMenuRepository;
import com.yd.telescope.system.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    TreeMenuRepository treeMenuRepository;

    @Override
    public List<Menu> findAllByPaging(MenuCondition condition) throws ServiceException {
        try {
            Specification<Menu> specification = new Specification<Menu>() {
                @Override
                public Predicate toPredicate(Root<Menu> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    return null;
                }
            };
            //创建分页对象
            PageRequest pageRequest = new PageRequest(condition.getPage(),condition.getSize());
            List<Menu> menus = menuRepository.findAll(specification,pageRequest).getContent();
            return menus;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public List<Menu> findAll() throws ServiceException {
        try {
            return menuRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public boolean save(Menu menu) throws ServiceException {
        try {
            return menuRepository.saveAndFlush(menu) != null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public void delete(long menuId) throws ServiceException {
        try {
            menuRepository.delete(menuId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public boolean exists(long menuId) throws ServiceException {
        try {
            return menuRepository.exists(menuId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public Menu findByMenuid(long menuId) throws ServiceException {
        try {
            return menuRepository.findOne(menuId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public void update(Menu menu) throws ServiceException {
        try {
            menuRepository.update(menu.getMenu_id(),menu.getName(),menu.getUrl(),menu.getPerms(),
                    menu.getIcon(),menu.getModify_time(),menu.getModifier());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }

    @Override
    public Set<Menu> findByMenu_ids(List<Integer> menu_ids) throws ServiceException {
        try {
            return menuRepository.findByMenu_ids(menu_ids);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }
    @Override
    public List<TreeMenu> findByParent_id(long parent_id) throws ServiceException {
        try {
            return treeMenuRepository.findByParent_id(parent_id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ResultEnums.DATABASE_OPERATION_ERROR);
        }
    }
}

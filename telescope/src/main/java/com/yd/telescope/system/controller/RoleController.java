package com.yd.telescope.system.controller;

import com.yd.telescope.common.dto.DatatableRes;
import com.yd.telescope.common.dto.JsonResult;
import com.yd.telescope.common.enums.ResultEnums;
import com.yd.telescope.common.exception.ServiceException;
import com.yd.telescope.common.util.ResultUtils;
import com.yd.telescope.system.domain.*;
import com.yd.telescope.system.service.MenuService;
import com.yd.telescope.system.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @RequestMapping("/system/role/list")
    @ResponseBody
    public DatatableRes<List<Role>> list(RoleCondition condition) throws ServiceException {
        return roleService.findAllByPaging(condition);
    }

    @RequestMapping(value = "/system/role/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult add(@Valid Role role , BindingResult bindingResult) throws ServiceException {
        if(bindingResult.hasErrors()){
            return ResultUtils.result(ResultEnums.PARAMETER_ERROR);
        }
        if(roleService.exists(role.getRole_id())){
            return ResultUtils.result(ResultEnums.EXIST);
        }else {
            return roleService.save(role)? ResultUtils.result(ResultEnums.SUCCESS):ResultUtils.result(ResultEnums.DATABASE_OPERATION_ERROR);
        }

    }

    @RequestMapping(value = "/system/role/delete", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult delete(@RequestBody String role_id) throws ServiceException {
        if(roleService.exists(role_id)){
            roleService.delete(role_id);
            return ResultUtils.result(ResultEnums.SUCCESS);
        }else {
            return ResultUtils.result(ResultEnums.NOT_EXIST);
        }
    }

    @RequestMapping(value = "/system/role/update/view")
    public String toUpdateView(Role role, Model model) {
        model.addAttribute("role",role);
        return "/system/role-update";
    }

    @RequestMapping(value = "/system/role/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult update(@Valid Role role, BindingResult bindingResult, HttpSession session)throws ServiceException {
        if(bindingResult.hasErrors()){
            return ResultUtils.result(ResultEnums.PARAMETER_ERROR);
        }
        if(roleService.exists(role.getRole_id())){
            role.setModify_time(new Timestamp(System.currentTimeMillis()));
            role.setModifier(((User)session.getAttribute("userinfo")).getUsername());
            roleService.update(role);
            return ResultUtils.result(ResultEnums.SUCCESS);
        }else {
            return ResultUtils.result(ResultEnums.NOT_EXIST);
        }
    }


    @RequestMapping(value = "/system/role/authorize/menus")
    @ResponseBody
    public  List<TreeMenu> menus(String role_id) throws ServiceException {
        Role role = roleService.findByRole_id(role_id);
        Set<Menu> roleMenus = role.getMenus();
        List<TreeMenu> menus = menuService.findByParent_id(0);
        getMenus(menus,roleMenus);
        return menus;
    }

    private void getMenus(List<TreeMenu> menus, Set<Menu> roleMenus) {
        if(null !=menus || menus.size() > 0 && null != roleMenus || roleMenus.size() > 0){
            for (int i = 0; i < menus.size() ; i++) {
                for (Menu roleMenu : roleMenus) {
                    if( menus.get(i).getNodeId() == roleMenu.getMenu_id()){
                        menus.get(i).getState().setChecked(true);
                        break;
                    }
                }
                List<TreeMenu> nodes = menus.get(i).getNodes();
                getMenus(nodes, roleMenus);
            }
        }
    }


    @RequestMapping(value = "/system/role/authorize/view")
    public String toAuthorizeView(String role_id, Model model) throws ServiceException {
            model.addAttribute("role_id",role_id);
        return "/system/role-authorize";
    }

    @RequestMapping(value = "/system/role/authorize")
    @ResponseBody
    public JsonResult authorize(@RequestParam(value = "menu_ids[]",defaultValue = "-1")List<Integer> menu_ids, String role_id) throws ServiceException {
        if(!StringUtils.isEmpty(role_id)){
            Role role = roleService.findByRole_id(role_id);
            if(null == menu_ids || menu_ids.get(0) == -1){
                role.setMenus(new HashSet<Menu>());
            }else {
                Set<Menu> insertRoles = menuService.findByMenu_ids(menu_ids);
                role.setMenus(insertRoles);
            }
            roleService.update(role);
            return ResultUtils.result(ResultEnums.SUCCESS);
        }else {
            return ResultUtils.result(ResultEnums.NOT_EXIST);
        }
    }

}

package com.yd.telescope.system.controller;

import com.yd.telescope.common.dto.DatatableRes;
import com.yd.telescope.common.dto.JsonResult;
import com.yd.telescope.common.enums.ResultEnums;
import com.yd.telescope.common.exception.ServiceException;
import com.yd.telescope.common.util.ResultUtils;
import com.yd.telescope.system.domain.Role;
import com.yd.telescope.system.domain.User;
import com.yd.telescope.system.domain.UserCondition;
import com.yd.telescope.system.service.RoleService;
import com.yd.telescope.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @RequestMapping("/system/user/list")
    @ResponseBody
    public DatatableRes<List<User>> list(UserCondition condition) throws ServiceException {

        return userService.findAllByPaging(condition);
    }

    @RequestMapping(value = "/system/user/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult add(@Valid User user , BindingResult bindingResult) throws ServiceException {
        if(bindingResult.hasErrors()){
            return ResultUtils.result(ResultEnums.PARAMETER_ERROR);
        }
        if(userService.exists(user.getUsername())){
            return ResultUtils.result(ResultEnums.EXIST);
        }else {
            user.setLoginattemps(0);
            user.setStatus("0");
            return userService.save(user)? ResultUtils.result(ResultEnums.SUCCESS):ResultUtils.result(ResultEnums.DATABASE_OPERATION_ERROR);
        }

    }

    @RequestMapping(value = "/system/user/update/view")
    public String toUpdateView(String  username, Model model) throws ServiceException {
        User user = userService.findByUsername(username);
        if(null != user){
            model.addAttribute("user", user);
        }
        return "/system/user-update";
    }

    @RequestMapping(value = "/system/user/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult update(@Valid User user, BindingResult bindingResult, HttpSession session)throws ServiceException {
        if(bindingResult.hasErrors()){
            return ResultUtils.result(ResultEnums.PARAMETER_ERROR);
        }
        if(userService.exists(user.getUsername())){
            user.setModify_time(new Timestamp(System.currentTimeMillis()));
            user.setModifier(((User)session.getAttribute("userinfo")).getUsername());
            userService.update(user);
            return ResultUtils.result(ResultEnums.SUCCESS);
        }else {
            return ResultUtils.result(ResultEnums.NOT_EXIST);
        }
    }

    @RequestMapping(value = "/system/user/delete", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult delete(@RequestBody User user) throws ServiceException {
        if(userService.exists(user.getUsername())){
            userService.delete(user.getUsername());
            return ResultUtils.result(ResultEnums.SUCCESS);
        }else {
            return ResultUtils.result(ResultEnums.NOT_EXIST);
        }
    }

    @RequestMapping(value = "/system/user/authorize/view")
    public String toAuthorizeView(String username, Model model) throws ServiceException {
        if(!StringUtils.isEmpty(username)){
            List<Role> roles = roleService.findAll();
            User user = userService.findByUsername(username);
            Set<Role> userRoles = user.getRoles();
            if(null != userRoles && null != roles){
                for (Role role: roles) {
                    if(userRoles.contains(role)){
                        role.setChecked(true);
                    }
                }
            }
            model.addAttribute("roles",roles);
            model.addAttribute("username",username);
        }
        return "/system/user-authorize";
    }

    @RequestMapping(value = "/system/user/authorize")
    @ResponseBody
    public JsonResult authorize(String[] role_ids,String username) throws ServiceException {
        if(!StringUtils.isEmpty(username)){
            User user = userService.findByUsername(username);
            if(null == role_ids || role_ids.length == 0){
                user.setRoles(new HashSet<Role>());
            }else {
                Set<Role> insertRoles = roleService.findByRole_ids(Arrays.asList(role_ids));
                user.setRoles(insertRoles);
            }
            userService.update(user);
            return ResultUtils.result(ResultEnums.SUCCESS);
        }else {
            return ResultUtils.result(ResultEnums.NOT_EXIST);
        }

    }

}

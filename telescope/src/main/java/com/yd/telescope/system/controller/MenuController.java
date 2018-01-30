package com.yd.telescope.system.controller;

import com.yd.telescope.common.dto.JsonResult;
import com.yd.telescope.common.enums.ResultEnums;
import com.yd.telescope.common.exception.ServiceException;
import com.yd.telescope.common.util.ResultUtils;
import com.yd.telescope.system.domain.Menu;
import com.yd.telescope.system.domain.User;
import com.yd.telescope.system.service.MenuService;
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
import java.util.List;

@Controller
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping("/system/menu/list")
    @ResponseBody
    public List<Menu> list() throws ServiceException {
        return menuService.findAll();
    }

    @RequestMapping(value = "/system/menu/add", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult add(@Valid Menu menu, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            return ResultUtils.result(ResultEnums.PARAMETER_ERROR);
        }
        return menuService.save(menu) ? ResultUtils.result(ResultEnums.SUCCESS) : ResultUtils.result(ResultEnums.DATABASE_OPERATION_ERROR);

    }

    @RequestMapping(value = "/system/menu/update/view")
    public String toUpdateView(long menu_id, Model model) throws ServiceException {
        Menu menu = menuService.findByMenuid(menu_id);
        if (menu != null)
            model.addAttribute("menu", menu);
        return "/system/menu-update";
    }

    @RequestMapping(value = "/system/menu/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult update(@Valid Menu menu, BindingResult bindingResult, HttpSession session) throws ServiceException {
        if (bindingResult.hasErrors()) {
            return ResultUtils.result(ResultEnums.PARAMETER_ERROR);
        }
        if (menuService.exists(menu.getMenu_id())) {
            menu.setModify_time(new Timestamp(System.currentTimeMillis()));
            menu.setModifier(((User) session.getAttribute("userinfo")).getUsername());
            menuService.update(menu);
            return ResultUtils.result(ResultEnums.SUCCESS);
        } else {
            return ResultUtils.result(ResultEnums.NOT_EXIST);
        }
    }

    @RequestMapping(value = "/system/menu/delete", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult delete(@RequestBody Menu menu) throws ServiceException {
        if (menuService.exists(menu.getMenu_id())) {
            menuService.delete(menu.getMenu_id());
            return ResultUtils.result(ResultEnums.SUCCESS);
        } else {
            return ResultUtils.result(ResultEnums.NOT_EXIST);
        }
    }

}

package com.yd.telescope.management.controller;

import com.yd.telescope.common.dto.JsonResult;
import com.yd.telescope.common.enums.ResultEnums;
import com.yd.telescope.common.exception.EncryException;
import com.yd.telescope.common.exception.ServiceException;
import com.yd.telescope.common.util.EncryUtils;
import com.yd.telescope.common.util.ResultUtils;
import com.yd.telescope.management.domain.App;
import com.yd.telescope.management.service.AppService;
import com.yd.telescope.system.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Controller
@SessionAttributes("userinfo")
public class OverviewController {

    @Autowired
    private AppService appService;
    @RequestMapping("/{appType}/overview")
    public String overview(@PathVariable String appType, Model model, HttpSession session) throws ServiceException {
        appType = appType.equals("mobile")?"M":"C";
        List<App> apps = appService.findByApptypeAndUsername(appType,((User)session.getAttribute("userinfo")).getUsername());
        if(null != apps){
            model.addAttribute("apps",apps);
            session.setAttribute("apps",apps);
        }
        return "management/overview";
    }

    @RequestMapping("/{appType}/dashboard")
    public String dashboard(@PathVariable String appType, ModelAndView model){
        return "/management/dashboard";
    }

    @RequestMapping("/{appType}/app/add/view")
    public String toAddView(@PathVariable String appType){
        return "/management/app-add";
    }

    @RequestMapping("/{appType}/app/add")
    @ResponseBody
    public JsonResult add(@PathVariable String appType, @Valid App app, BindingResult bindingResult, @ModelAttribute("userinfo") User user) throws EncryException, ServiceException {

        if(bindingResult.hasErrors()){
            return ResultUtils.result(ResultEnums.PARAMETER_ERROR);
        }

        if(!appService.exists(user.getUsername(),app.getAppname(),app.getOstype())){
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");//生成uuid
            app.setAppid(uuid);
            String appKey = EncryUtils.EncoderByMd5ForHex((app.getAppname()+System.currentTimeMillis() + user.getUsername()));
            app.setUsername(user.getUsername());
            app.setAppkey(appKey);
            app.setStatus("0");
            return appService.save(app)? ResultUtils.result(ResultEnums.SUCCESS) : ResultUtils.result(ResultEnums.DATABASE_OPERATION_ERROR);
        }else{
            return ResultUtils.result(ResultEnums.EXIST);
        }
    }

    @RequestMapping(value = "/{appType}/app/delete", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult delete(@PathVariable String appType,@RequestBody String appid) throws ServiceException {
        if(appService.exists(appid)){
            appService.delete(appid);
            return ResultUtils.result(ResultEnums.SUCCESS);
        }else {
            return ResultUtils.result(ResultEnums.NOT_EXIST);
        }
    }

    @RequestMapping(value = "/{appType}/app/update/view")
    public String toUpdateView(@PathVariable String appType,String appid, Model model) throws ServiceException {
        App app = appService.findByAppid(appid);
        model.addAttribute("app",app);
        return "/management/app-update";
    }

    @RequestMapping(value = "/{appType}/app/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult update(@PathVariable String appType,App app,HttpSession session)throws ServiceException {

        if(appService.exists(app.getAppid())){
            app.setModify_time(new Timestamp(System.currentTimeMillis()));
            app.setModifier(((User)session.getAttribute("userinfo")).getUsername());
            appService.update(app);
            return ResultUtils.result(ResultEnums.SUCCESS);
        }else {
            return ResultUtils.result(ResultEnums.NOT_EXIST);
        }
    }





}

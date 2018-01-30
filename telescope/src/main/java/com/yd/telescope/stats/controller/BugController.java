package com.yd.telescope.stats.controller;

import com.yd.telescope.common.dto.DatatableRes;
import com.yd.telescope.common.dto.JsonResult;
import com.yd.telescope.common.exception.ESRestException;
import com.yd.telescope.common.util.ResultUtils;
import com.yd.telescope.stats.controller.respose.BugDetailRep;
import com.yd.telescope.stats.controller.respose.BugFatalVisualizeRep;
import com.yd.telescope.stats.controller.restApi.BugApi;
import com.yd.telescope.stats.controller.util.ConstantUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: bug统计绘图
 * @author: zygong
 * @date: 2018/1/22 11:02
 */
@Controller
public class BugController {

    @Autowired
    private BugApi bugApi;

    /**
     * @param appid:    应用id
     * @param version:  添加默认All
     * @param duration: 时间区间
     * @param model:    model
     * @Description 获取版本列表
     * @Anthor: zygong
     * @Return: java.lang.String
     * @Datatime: 2018/1/22 10:16
     */
    @RequestMapping("/{appType}/app/{appid}/stats/bug")
    public String bug(@PathVariable String appType, @PathVariable String appid, @RequestParam String version, @RequestParam String duration, Model model) throws ESRestException {

        //dummy data
        List<String> versions = bugApi.versionList(appid);
        model.addAttribute("versions", versions);
        model.addAttribute("duration", duration);
        model.addAttribute("version", version);
        return "stats/bug";
    }


    /**
     * @param appid:    应用id
     * @param version:  应用版本
     * @param duration: 时间区间
     * @param type:     统计维度：崩溃汇总、版本分布、设备分布、系统分布
     * @param model:    model
     * @Description 获取崩溃一览列表
     * @Anthor: zygong
     * @Return: com.yd.telescope.common.dto.JsonResult
     * @Datatime: 2018/1/22 10:17
     */
    @ResponseBody
    @RequestMapping("/{appType}/app/{appid}/stats/bug-fatal-percent")
    public JsonResult bug_fatal_percent(@PathVariable String appType, @PathVariable String appid, @RequestParam String version, @RequestParam String duration, @RequestParam String type, Model model) throws ESRestException {

        Map<String, Object> result = bugApi.bug_fatal_percent(switchDimension(type), mapData(appid, version, type, null), "now-" + duration, "now");

        return ResultUtils.success(result);
    }


    /**
     * @param appid:     应用id
     * @param version:   应用版本
     * @param duration:  时间区间
     * @param type:      统计维度：崩溃汇总、版本分布、设备分布、系统分布
     * @param typeValue: 统计维度相对应的值
     * @param model:     model
     * @Description 统计
     * @Anthor: zygong
     * @Return: com.yd.telescope.common.dto.JsonResult
     * @Datatime: 2018/1/22 10:20
     */
    @ResponseBody
    @RequestMapping("/{appType}/app/{appid}/stats/bug-fatal-show")
    public JsonResult bug_fatal_show(@PathVariable String appType, @PathVariable String appid, @RequestParam String version, @RequestParam String duration, @RequestParam String type,
                                     @RequestParam(required = false) String typeValue, Model model) throws ESRestException {

        Map<String, Object> result = bugApi.count(mapData(appid, version, type, typeValue), "now-" + duration, "now");

        return ResultUtils.success(result);
    }

    /**
     * @param appid:     应用id
     * @param version:   应用版本
     * @param duration:  时间区间
     * @param type:      统计维度：崩溃汇总、版本分布、设备分布、系统分布
     * @param typeValue: 统计维度相对应的值
     * @param size:      分页size
     * @param page:      当前页
     * @param model:     model
     * @Description bug列表
     * @Anthor: zygong
     * @Return: com.yd.telescope.common.dto.DatatableRes<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     * @Datatime: 2018/1/22 10:22
     */
    @ResponseBody
    @RequestMapping("/{appType}/app/{appid}/stats/bug-fatal-list")
    public DatatableRes<List<Map<String, Object>>> bug_fatal_list(@PathVariable String appType, @PathVariable String appid, @RequestParam String version,
                                                                  @RequestParam String duration, @RequestParam String type,
                                                                  @RequestParam(required = false) String typeValue, @RequestParam int size,
                                                                  @RequestParam int page, Model model) throws ESRestException {

        DatatableRes<List<Map<String, Object>>> pagingObject = bugApi.bug_fatal_list(mapData(appid, version, type, typeValue), size, page * size, "now-" + duration, "now");

        return pagingObject;
    }

    /**
     * @param appid:     应用id
     * @param version:   应用版本
     * @param duration:  时间区间
     * @param type:      统计维度：崩溃汇总、版本分布、设备分布、系统分布
     * @param typeValue: 统计维度相对应的值
     * @param model:     model
     * @Description 图标
     * @Anthor: zygong
     * @Return: com.yd.telescope.common.dto.JsonResult
     * @Datatime: 2018/1/22 10:24
     */
    @ResponseBody
    @RequestMapping("/{appType}/app/{appid}/stats/bug-fatal-visualize")
    public JsonResult bug_fatal_visualize(@PathVariable String appType, @PathVariable String appid, @RequestParam String version, @RequestParam String duration, @RequestParam String type, @RequestParam(required = false) String typeValue, Model model) throws ESRestException {

        BugFatalVisualizeRep rep = bugApi.bug_fatal_visualize(switchDimension(type), mapData(appid, version, type, typeValue), 5, duration);

        return ResultUtils.success(rep);
    }

    /**
     * @Description bug详情
     * @Anthor: zygong
     * @Param appType: app或者客户端
     * @Param appid: appid
     * @Param index: 索引
     * @Param id: 文档数据id
     * @Param model:  model
     * @Return: java.lang.String
     * @Datatime: 2018/1/23 15:08
     */
    @RequestMapping("/{appType}/app/{appid}/stats/bug/{index}/{id}")
    public String bug_fatal_bugDetail(@PathVariable String appType, @PathVariable String appid, @PathVariable String index, @PathVariable String id, Model model) throws ESRestException {

        BugDetailRep rep = bugApi.bug_fatal_bugDetail(index, id);
        rep.setT_stackinfo(rep.getT_stackinfo().replace("    ","\r\n"));
        model.addAttribute("rep", rep);
       // System.out.println(JSON.toJSONString(rep,true));
        return "/stats/bug-info";
    }

    /**
     * @param appid:   应用id
     * @param version: 版本
     * @Description 组装map
     * @Anthor: zygong
     * @Return: java.util.Map<java.lang.String,java.lang.Object>
     * @Datatime: 2018/1/22 10:25
     */
    public Map<String, Object> mapData(String appid, String version, String type, String typeValue) {

        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(appid)) {
            map.put("initInfo.t_app_id", appid);
        }
        if (StringUtils.isNotBlank(version) && !"All".equals(version)) {
            map.put("initInfo.t_app_ver", version);
        }
        if (!ConstantUtil.FATAL.equals(type) && StringUtils.isNotBlank(typeValue)) {
            map.put(switchDimension(type), typeValue);
        }
        return map;
    }

    /**
     * @param type:
     * @Description 匹配对应字段
     * @Anthor: zygong
     * @Return: java.lang.String
     * @Datatime: 2018/1/22 10:26
     */
    public String switchDimension(String type) {

        switch (type) {
            case "fatal":
                type = "fatal";
                break;
            case "version":
                type = "initInfo.t_app_ver";
                break;
            case "device":
                type = "initInfo.t_dev_name";
                break;
            case "os":
                type = "initInfo.t_os_type";
                break;
        }
        return type;
    }
}

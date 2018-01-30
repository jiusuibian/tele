package com.yd.telescopeapi.controllerTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yd.telescopeapi.domain.Initinfo;
import com.yd.telescopeapi.domain.ViewInfo;
import com.yd.telescopeapi.repository.InitinfoRepository;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 页面操作接口测试
 *
 * @author zygong
 * @create 2017-12-26 17:20
 **/

public class ViewInfoCtlTest extends CtrlBaseTest {

    private final static String[] evts = {"查看","搜索", "发现", "我的", "贷款", "定期", "货基", "保险", "新手福利", "员工理财", "立享", "更多",
                                            "提现", "充值", "我的网贷", "我的定期", "我的货基", "我的立享", "我的保险", "我的卡券", "我的银行卡",
                                            "邀请有奖", "会员中心", "帮助反馈", "设置", "交易密码", "修改登录密码", "设置锁屏", "关于我们"};

    private List<String> activityNameList = new ArrayList<>();

    @Autowired
    private InitinfoRepository initinfoRepository;


    private Gson gson = new GsonBuilder().create();

    @Test
    public void viewInfoTest() throws Exception {

        // 从文件中获取activity
        String activityPath = "E:\\Document\\acitivity.txt";
        String fragmentPath = "E:\\Document\\fragment.txt";
        readTxt(activityPath);
        readTxt(fragmentPath);

        List<ViewInfo> mapList =  new ArrayList<>();
        ViewInfo viewInfo = null;
        Map<String, Object> map = null;
        String userId = uuid();

        List<Initinfo> initInfoList = initinfoRepository.findAll();
        int i = 0;
        for(Initinfo initinfo : initInfoList){
            int item = 0;
//            while(item < 50){
                setData(userId, initinfo.getT_app_id(), initinfo.getT_dev_id(), mapList, viewInfo);
                item++;
//            }
            MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/viewInfo").header("t_app_id", initinfo.getT_app_id()).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(mapList)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andReturn();

            System.out.println(result.getResponse().getContentAsString());
            mapList.clear();
            if(++i > 20){
                break;
            }else{
                Thread.sleep(500);
            }
            break;
        }

    }

    private void setData(String userId, String appId, String devId,  List<ViewInfo> mapList, ViewInfo viewInfo) throws Exception {
        viewInfo = new ViewInfo();
        viewInfo.setT_dev_id(devId);
        viewInfo.setT_est(getEst());
        viewInfo.setT_evt(getEvt());
        viewInfo.setT_page(getPage());
        viewInfo.setT_time(randomDate());
//        viewInfo.setT_time("2015-11-12T03:14:41.435Z");
        viewInfo.setT_user_id(userId);
        mapList.add(viewInfo);
    }

    /**
     *    请求响应时间
     *
     * @author zygong
     * @date 2017/12/27 13:19
     * @param []
     * @return int:
     */
    private int getEst(){
        int i = random.nextInt(200);
        if(i<10){
            return i+1000;
        }else{
            return i;
        }
    }

    private String randomDate(){
        Date date = null;
        int year = 2010 + random.nextInt(7);
        int month = 1 + random.nextInt(11);
        int day = 1 + random.nextInt(27);
        String dateStr = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day) + " " + String.format("%02d", random.nextInt(24)) + ":" + String.format("%02d", random.nextInt(59)) + ":" + String.format("%02d", random.nextInt(59));
        return dateStr;
    }

    /**
     *    获取点击事件
     *
     * @author zygong
     * @date 2017/12/27 13:05
     * @param []
     * @return java.lang.String:
     */
    private String getEvt(){
        return evts[random.nextInt(evts.length - 1)];
    }

    /**
     *    获取页面
     *
     * @author zygong
     * @date 2017/12/27 13:01
     * @param [activityNameList]
     * @return java.lang.String:
     */
    private String getPage(){
        return activityNameList.get(random.nextInt(activityNameList.size() - 1));
    }

    /**
     *    从txt文件中获取所有的页面
     *
     * @author zygong
     * @date 2017/12/27 13:02
     * @param [activityNameList, activityPath]
     * @return void:
     */
    private void readTxt(String activityPath) throws IOException {
        FileReader fr = new FileReader(activityPath);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        while(StringUtils.isNotBlank(line = br.readLine())){
            activityNameList.add(getClassName(line));
        }
    }

    /**
     *    从txt文件中获取页面名称
     *
     * @author zygong
     * @date 2017/12/27 13:02
     * @param [line]
     * @return java.lang.String:
     */
    private String getClassName(String line){
        String className = line.substring(line.lastIndexOf("\\") + 1, line.indexOf("."));
        return className;
    }
}

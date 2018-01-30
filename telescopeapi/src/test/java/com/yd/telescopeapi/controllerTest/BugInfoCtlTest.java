package com.yd.telescopeapi.controllerTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yd.telescopeapi.domain.BugInfo;
import com.yd.telescopeapi.domain.CrashSteps;
import com.yd.telescopeapi.domain.Initinfo;
import com.yd.telescopeapi.domain.ViewInfo;
import com.yd.telescopeapi.repository.InitinfoRepository;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * bug崩溃接口
 *
 * @author zygong
 * @create 2017-12-27 15:07
 **/
public class BugInfoCtlTest extends CtrlBaseTest {

    private final static String[] evts = {"查看", "搜索", "发现", "我的", "贷款", "定期", "货基", "保险", "新手福利", "员工理财", "立享", "更多",
            "提现", "充值", "我的网贷", "我的定期", "我的货基", "我的立享", "我的保险", "我的卡券", "我的银行卡",
            "邀请有奖", "会员中心", "帮助反馈", "设置", "交易密码", "修改登录密码", "设置锁屏", "关于我们"};

    private final static String[] telecomOpts = {"中国移动", "中国联通", "中国电信"};

    private final static String[] netModes = {"3G", "4G", "wifi"};

    private final static String ip = "127.0.0.1";

    private List<String> activityNameList = new ArrayList<>();

    @Autowired
    private InitinfoRepository initinfoRepository;

    private Gson gson = new GsonBuilder().create();

    private List<Initinfo> initInfoList = null;

    private void getActivity() throws IOException {
        // 从文件中获取activity
        String activityPath = "E:\\Document\\acitivity.txt";
        String fragmentPath = "E:\\Document\\fragment.txt";
        readTxt(activityPath);
        readTxt(fragmentPath);
    }

    private List<CrashSteps> viewInfo(Initinfo initinfo) throws Exception {

        List<CrashSteps> mapList = new ArrayList<>();
        CrashSteps crashSteps = null;
        Map<String, Object> map = null;
        String userId = uuid();

        int item = 0;
        while (item < 10) {
            setData(userId, initinfo.getT_app_id(), initinfo.getT_dev_id(), mapList, crashSteps);
            item++;
        }
        return mapList;
    }

    @Test
    public void getBug() throws Exception {

        initInfoList = initinfoRepository.findAll();
        getActivity();

        BugInfo bugInfo = null;
        StringBuffer sb = null;
        String bugType = "";
        String firstLine = "";
        Initinfo initinfo = null;
        for (int i = 1; i <= 2; i++) {
            sb = new StringBuffer();
            boolean isFirst = true;

            String bugPath = "E:\\Document\\" + random.nextInt(6) + ".txt";
            FileReader fr = new FileReader(bugPath);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while (StringUtils.isNotBlank(line = br.readLine())) {
                if (isFirst) {
                    firstLine = line;
                }
                sb.append(line);
                isFirst = false;
            }

            int maxJ = 1 + random.nextInt(3);
            for (int j = 0; j < maxJ; j++) {
                initinfo = initInfoList.get(random.nextInt(initInfoList.size()));
                bugInfo = setData(sb, firstLine, initinfo);

                post(bugInfo, initinfo);
                break;
            }
            break;
        }
    }

    private BugInfo setData(StringBuffer sb, String firstLine, Initinfo initinfo) throws Exception {
        BugInfo bugInfo;
        bugInfo = new BugInfo();
        bugInfo.setT_app_id(initinfo.getT_app_id());
        bugInfo.setT_bug_desc(getDes(firstLine));
        bugInfo.setT_cpu_usage(randomInt2());
        bugInfo.setT_crash_steps(viewInfo(initinfo));
        bugInfo.setT_dev_id(initinfo.getT_dev_id());
        bugInfo.setT_free_mem(randomInt());
        bugInfo.setT_free_power(randomInt2());
        bugInfo.setT_free_space(randomInt());
        bugInfo.setT_ip(ip);
        bugInfo.setT_net_mode(getRandomType(netModes));
        bugInfo.setT_net_speed(randomInt());
        bugInfo.setT_stackinfo(sb.toString());
        bugInfo.setT_telecom_opt(getRandomType(telecomOpts));
        bugInfo.setT_time(randomDate());
        bugInfo.setT_bug_attach(getTitle(firstLine));
        return bugInfo;
    }

    private void post(BugInfo bugInfo, Initinfo initinfo) throws Exception {
        System.out.println(gson.toJson(bugInfo));
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/bugInfo").header("t_app_id", initinfo.getT_app_id()).contentType(MediaType.APPLICATION_JSON_UTF8).content(gson.toJson(bugInfo)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    private String getRandomType(String[] strs) {
        return strs[random.nextInt(strs.length - 1)];
    }

    /**
     * 获取异常表述
     *
     * @param [firstLine]
     * @return java.lang.String:
     * @author zygong
     * @date 2017/12/28 10:37
     */
    private String getDes(String firstLine) {
        return firstLine.substring(firstLine.lastIndexOf(":") + 1);
    }

    /**
     * 获取异常类型
     *
     * @param [firstLine]
     * @return java.lang.String:
     * @author zygong
     * @date 2017/12/28 10:26
     */
    private String getTitle(String firstLine) {
        StringBuffer sb = null;
        Pattern pattern = Pattern.compile("(?<=:)(.+)(?=:)");
        Matcher matcher = pattern.matcher(firstLine);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    private int randomInt2() {
        return 50 + random.nextInt(40);
    }

    private int randomInt() {
        return 100 + random.nextInt(2000);
    }

    private void setData(String userId, String appId, String devId, List<CrashSteps> mapList, CrashSteps crashSteps) throws Exception {
        crashSteps = new CrashSteps();
        crashSteps.setT_evt(getEvt());
        crashSteps.setT_page(getPage());
        crashSteps.setT_time(randomDate());
        mapList.add(crashSteps);
    }

    /**
     * 请求响应时间
     *
     * @param []
     * @return int:
     * @author zygong
     * @date 2017/12/27 13:19
     */
    private int getEst() {
        int i = random.nextInt(200);
        if (i < 10) {
            return i + 1000;
        } else {
            return i;
        }
    }

    private String randomDate() {
        Date date = null;
        int year = 2010 + random.nextInt(7);
        int month = 1 + random.nextInt(11);
        int day = 1 + random.nextInt(27);
        String dateStr = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day) + " " + String.format("%02d", random.nextInt(24)) + ":" + String.format("%02d", random.nextInt(59)) + ":" + String.format("%02d", random.nextInt(59));
        return dateStr;
    }

    private String randomDate2() {
        Date date = null;
        int year = 2010 + random.nextInt(7);
        int month = 1 + random.nextInt(11);
        int day = 1 + random.nextInt(27);

        String dateStr = year + "-" + month + "-" + day + " " + random.nextInt(24) + ":" + random.nextInt(59) + ":" + random.nextInt(59);
        return dateStr;
    }

    /**
     * 获取点击事件
     *
     * @param []
     * @return java.lang.String:
     * @author zygong
     * @date 2017/12/27 13:05
     */
    private String getEvt() {
        return evts[random.nextInt(evts.length - 1)];
    }

    /**
     * 获取页面
     *
     * @param [activityNameList]
     * @return java.lang.String:
     * @author zygong
     * @date 2017/12/27 13:01
     */
    private String getPage() {
        return activityNameList.get(random.nextInt(activityNameList.size() - 1));
    }

    /**
     * 从txt文件中获取所有的页面
     *
     * @param [activityNameList, activityPath]
     * @return void:
     * @author zygong
     * @date 2017/12/27 13:02
     */
    private void readTxt(String activityPath) throws IOException {
        FileReader fr = new FileReader(activityPath);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        while (StringUtils.isNotBlank(line = br.readLine())) {
            activityNameList.add(getClassName(line));
        }
    }

    /**
     * 从txt文件中获取页面名称
     *
     * @param [line]
     * @return java.lang.String:
     * @author zygong
     * @date 2017/12/27 13:02
     */
    private String getClassName(String line) {
        String className = line.substring(line.lastIndexOf("\\") + 1, line.indexOf("."));
        return className;
    }
}

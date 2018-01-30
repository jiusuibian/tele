package com.yd.telescopeapi.controllerTest;

import net.minidev.json.JSONObject;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

/**
 * 手机信息接口测试类
 *
 * @author zygong
 * @create 2017-12-22 13:35
 **/
public class InitInfoCtlTest extends CtrlBaseTest{

    private final static String[] devNames = { "三星 Galaxy C9 Pro", "Galaxy C5 Pro SM-C5010",
            "小米手机6", "小米手机5","华为 P10 Plus","华为 Mate 9 Pro",
            "荣耀 v10","荣耀 9","vivo x9s", "vivo x20","oppo r11s", "oppo a79",};

    private final static String[] osTypes = { "Android 5.0","Android 6.0","Android 7.0",};

    private final static String[] cpuSets = {"armv6", "armv7", "armv7s", "arm64", "ARMv5", "ARMv8", "X86"};

    private final static String[] cpuTypes = {"骁龙801(MSM8x74AA)","骁龙805(APQ8084)", "骁龙808(MSM8992)", "骁龙810( MSM8994)", "骁龙820(MSM8996)", "骁龙821(MSM8996 Pro)", "骁龙835(MSM8998)", "骁龙610(MSM8936)", "骁龙615( MSM8939)",
            "骁龙650 (618)(MSM8956)", "骁龙626(MSM8953 Pro)", "骁龙400(MSM8x26)", "Exynos 9 Series", "Exynos 7 Series",
            "Kirin620", "Kirin650", "Kirin910", "Kirin920", "Kirin930", "Kirin950", "Kirin 960", "Exynos 7420"};

    private final static String[] versionTest = {"1.0.0","1.1.0", "1.2.0", "2.0.0"};

    private final static String[] sdkTest = {"2.0.0", "2.1.0"};
    @Test
    public void initInfoTest() throws Exception {
        int i = 0;
        while(i < 200){
            i++;
            initInfo();
            Thread.sleep(500);
        }
    }

    public void initInfo() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("t_os_type", osType());
        map.put("t_sdk_ver", appVerOrSdkVer(0));
        map.put("t_cpu_type", cpuType());
        map.put("t_cpu_set", cpuSet());
        map.put("t_isroot", randomBoolean());
        map.put("t_app_ver", appVerOrSdkVer(1));
        map.put("t_dev_id", uuid());
        map.put("t_gps_on", randomBoolean());
        map.put("t_dev_name", devName());

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/initInfo").header("t_app_id", "116e204a50cf4246bc08de38586ca083").contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(map)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    /**
     *    cpu指令集
     *
     * @author zygong
     * @date 2017/12/26 14:46
     * @param []
     * @return java.lang.String:
     */
    public String cpuSet(){
        int ints = random.nextInt(cpuSets.length);
        return cpuSets[ints];
    }

    /**
     *    cpu型号
     *
     * @author zygong
     * @date 2017/12/26 14:10
     * @param []
     * @return java.lang.String:
     */
    public String cpuType(){
        int ints = random.nextInt(cpuTypes.length);
        return cpuTypes[ints];
    }

    /**
     *    设备名称
     *
     * @author zygong
     * @date 2017/12/26 14:10
     * @param []
     * @return java.lang.String:
     */
    public String devName(){
        int ints = random.nextInt(devNames.length);
        return devNames[ints];
    }

    /**
     *    应用版本号、sdk版本
     *
     * @author zygong
     * @date 2017/12/26 14:10
     * @param []
     * @return java.lang.String:
     */
    public String appVerOrSdkVer(int i){
        if(i == 0){
            int osType = random.nextInt(sdkTest.length);
            return sdkTest[osType];
        }else {
            int osType = random.nextInt(versionTest.length);
            return versionTest[osType];
        }
    }

    /**
     *    系统版本号
     *
     * @author zygong
     * @date 2017/12/26 14:13
     * @param []
     * @return java.lang.String:
     */
    public String osType(){
        int osType = random.nextInt(osTypes.length);
        return osTypes[osType];
    }

}

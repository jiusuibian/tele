package com.yd.telescopeapi.domain;

/**
 * 页面操作
 *
 * @author zygong
 * @create 2017-12-25 10:17
 **/
public class ViewInfo {
    /**
     * 浏览页面
     */
    private String t_page;
    /**
     * 点击事件
     */
    private String t_evt;
    /**
     * 点击时间
     */
    private String t_time;
    /**
     * 设备id
     */
    private String t_dev_id;
    /**
     * APP_ID
     */
    private String t_app_id;
    /**
     * 用户ID
     */
    private String t_user_id;
    /**
     * 点击耗时
     */
    private long t_est;
    /**
     * 手机设备信息
     */
    private Initinfo initInfo;
    /**
     * 信息类型
     */
    private String t_infoType;

    public String getT_page() {
        return t_page;
    }

    public void setT_page(String t_page) {
        this.t_page = t_page;
    }

    public String getT_evt() {
        return t_evt;
    }

    public void setT_evt(String t_evt) {
        this.t_evt = t_evt;
    }

    public String getT_time() {
        return t_time;
    }

    public void setT_time(String t_time) {
        this.t_time = t_time;
    }

    public String getT_dev_id() {
        return t_dev_id;
    }

    public void setT_dev_id(String t_dev_id) {
        this.t_dev_id = t_dev_id;
    }

    public String getT_app_id() {
        return t_app_id;
    }

    public void setT_app_id(String t_app_id) {
        this.t_app_id = t_app_id;
    }

    public String getT_user_id() {
        return t_user_id;
    }

    public void setT_user_id(String t_user_id) {
        this.t_user_id = t_user_id;
    }

    public long getT_est() {
        return t_est;
    }

    public void setT_est(long t_est) {
        this.t_est = t_est;
    }

    public Initinfo getInitInfo() {
        return initInfo;
    }

    public void setInitInfo(Initinfo initInfo) {
        this.initInfo = initInfo;
    }

    public String getT_infoType() {
        return t_infoType;
    }

    public void setT_infoType(String t_infoType) {
        this.t_infoType = t_infoType;
    }
}

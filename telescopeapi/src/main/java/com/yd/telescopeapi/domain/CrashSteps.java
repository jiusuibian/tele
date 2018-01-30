package com.yd.telescopeapi.domain;

import java.util.Date;

/**
 * 崩溃轨迹
 *
 * @author zygong
 * @create 2017-12-25 9:53
 **/
public class CrashSteps {

    /**
     * 当前页面
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
}

package com.yd.telescopeapi.domain;

import java.util.List;

/**
 * 崩溃接收对象
 *
 * @author zygong
 * @create 2017-12-25 9:45
 **/
public class BugInfo {
    /**
     * 剩余内存
     */
    private double t_free_mem;
    /**
     * CPU使用率
     */
    private double t_cpu_usage;
    /**
     * 设备剩余空间
     */
    private double t_free_space;
    /**
     * 剩余电量
     */
    private double t_free_power;
    /**
     * 运营商
     */
    private String t_telecom_opt;
    /**
     * 接入方式
     */
    private String t_net_mode;
    /**
     * 堆栈数据
     */
    private String t_stackinfo;
    /**
     * 崩溃轨迹
     */
    private List<CrashSteps> t_crash_steps;
    /**
     * bug描述
     */
    private String t_bug_desc;
    /**
     * 附加信息
     */
    private String t_bug_attach;
    /**
     * ip地址
     */
    private String t_ip;
    /**
     * 发生时间
     */
    private String t_time;
    /**
     * 当前网速
     */
    private double t_net_speed;
    /**
     * 设备id
     */
    private String t_dev_id;
    /**
     * APP_ID
     */
    private String t_app_id;
    /**
     * 设备信息
     */
    private Initinfo initInfo;
    /**
     * 信息类型
     */
    private String t_infoType;
    /**
     * 0 未修复，1 已修复，2 已忽略，3 待验证
     */
    private int t_status;

    public double getT_free_mem() {
        return t_free_mem;
    }

    public void setT_free_mem(double t_free_mem) {
        this.t_free_mem = t_free_mem;
    }

    public double getT_cpu_usage() {
        return t_cpu_usage;
    }

    public void setT_cpu_usage(double t_cpu_usage) {
        this.t_cpu_usage = t_cpu_usage;
    }

    public double getT_free_space() {
        return t_free_space;
    }

    public void setT_free_space(double t_free_space) {
        this.t_free_space = t_free_space;
    }

    public double getT_free_power() {
        return t_free_power;
    }

    public void setT_free_power(double t_free_power) {
        this.t_free_power = t_free_power;
    }

    public String getT_telecom_opt() {
        return t_telecom_opt;
    }

    public void setT_telecom_opt(String t_telecom_opt) {
        this.t_telecom_opt = t_telecom_opt;
    }

    public String getT_net_mode() {
        return t_net_mode;
    }

    public void setT_net_mode(String t_net_mode) {
        this.t_net_mode = t_net_mode;
    }

    public String getT_stackinfo() {
        return t_stackinfo;
    }

    public void setT_stackinfo(String t_stackinfo) {
        this.t_stackinfo = t_stackinfo;
    }

    public List<CrashSteps> getT_crash_steps() {
        return t_crash_steps;
    }

    public void setT_crash_steps(List<CrashSteps> t_crash_steps) {
        this.t_crash_steps = t_crash_steps;
    }

    public String getT_bug_desc() {
        return t_bug_desc;
    }

    public void setT_bug_desc(String t_bug_desc) {
        this.t_bug_desc = t_bug_desc;
    }

    public String getT_bug_attach() {
        return t_bug_attach;
    }

    public void setT_bug_attach(String t_bug_attach) {
        this.t_bug_attach = t_bug_attach;
    }

    public String getT_ip() {
        return t_ip;
    }

    public void setT_ip(String t_ip) {
        this.t_ip = t_ip;
    }

    public String getT_time() {
        return t_time;
    }

    public void setT_time(String t_time) {
        this.t_time = t_time;
    }

    public double getT_net_speed() {
        return t_net_speed;
    }

    public void setT_net_speed(double t_net_speed) {
        this.t_net_speed = t_net_speed;
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

    public int getT_status() {
        return t_status;
    }

    public void setT_status(int t_status) {
        this.t_status = t_status;
    }
}

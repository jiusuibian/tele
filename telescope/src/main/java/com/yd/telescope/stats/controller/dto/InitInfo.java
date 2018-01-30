package com.yd.telescope.stats.controller.dto;

import java.util.Date;
import java.util.List;

public class InitInfo {

    private Long id;
    /**
     *APP_ID
     */
    private String t_app_id;
    /**
     *操作系统型号(带版本号)
     */
    private String t_os_type;
    /**
     *sdk版本
     */
    private String t_sdk_ver;
    /**
     *cpu型号
     */
    private String t_cpu_type;
    /**
     *cpu指令集
     */
    private String t_cpu_set;
    /**
     *是否root
     */
    private boolean t_isroot;
    /**
     *应用版本
     */
    private String t_app_ver;
    /**
     *首次记录时间
     */
    private Date t_up_time;
    /**
     *最新记录时间
     */
    private Date t_last_time;
    /**
     *GPS是否关闭
     */
    private boolean t_gps_on;
    /**
     * 设备名称
     */
    private String t_dev_name;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getT_app_id() {
        return t_app_id;
    }

    public void setT_app_id(String t_app_id) {
        this.t_app_id = t_app_id;
    }

    public String getT_os_type() {
        return t_os_type;
    }

    public void setT_os_type(String t_os_type) {
        this.t_os_type = t_os_type;
    }

    public String getT_sdk_ver() {
        return t_sdk_ver;
    }

    public void setT_sdk_ver(String t_sdk_ver) {
        this.t_sdk_ver = t_sdk_ver;
    }

    public String getT_cpu_type() {
        return t_cpu_type;
    }

    public void setT_cpu_type(String t_cpu_type) {
        this.t_cpu_type = t_cpu_type;
    }

    public String getT_cpu_set() {
        return t_cpu_set;
    }

    public void setT_cpu_set(String t_cpu_set) {
        this.t_cpu_set = t_cpu_set;
    }

    public boolean isT_isroot() {
        return t_isroot;
    }

    public void setT_isroot(boolean t_isroot) {
        this.t_isroot = t_isroot;
    }

    public String getT_app_ver() {
        return t_app_ver;
    }

    public void setT_app_ver(String t_app_ver) {
        this.t_app_ver = t_app_ver;
    }

    public Date getT_up_time() {
        return t_up_time;
    }

    public void setT_up_time(Date t_up_time) {
        this.t_up_time = t_up_time;
    }

    public Date getT_last_time() {
        return t_last_time;
    }

    public void setT_last_time(Date t_last_time) {
        this.t_last_time = t_last_time;
    }

    public String getT_dev_id() {
        return t_dev_id;
    }

    public void setT_dev_id(String t_dev_id) {
        this.t_dev_id = t_dev_id;
    }

    public boolean isT_gps_on() {
        return t_gps_on;
    }

    public void setT_gps_on(boolean t_gps_on) {
        this.t_gps_on = t_gps_on;
    }

    public String getT_dev_name() {
        return t_dev_name;
    }

    public void setT_dev_name(String t_dev_name) {
        this.t_dev_name = t_dev_name;
    }

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
}

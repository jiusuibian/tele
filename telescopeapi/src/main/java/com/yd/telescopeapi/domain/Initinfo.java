package com.yd.telescopeapi.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *    手机初始化信息
 *
 * @author zygong
 * @date 2017/12/22 13:02
 */
@Entity
@Table(name = "init_info")
public class Initinfo implements Serializable{

    @Id
    @GeneratedValue
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date t_up_time;
    /**
     *最新记录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date t_last_time;
    /**
     *设备号
     */
    private String t_dev_id;
    /**
     *GPS是否关闭
     */
    private boolean t_gps_on;
    /**
     * 设备名称
     */
    private String t_dev_name;

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

    public boolean getT_isroot() {
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

    public boolean getT_gps_on() {
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

}

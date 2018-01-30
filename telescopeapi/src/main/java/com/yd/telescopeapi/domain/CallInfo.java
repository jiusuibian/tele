package com.yd.telescopeapi.domain;

import java.util.Date;

/**
 * 客户端接口调用
 *
 * @author zygong
 * @create 2017-12-25 10:28
 **/
public class CallInfo {
    /**
     * APP_ID
     */
    private String t_app_id;
    /**
     * 设备号
     */
    private String t_dev_id;
    /**
     * 服务地址
     */
    private String t_srv_url;
    /**
     * 服务名称
     */
    private String t_srv_name;
    /**
     * 请求时间
     */
    private Date t_req_time;
    /**
     * 请求耗时
     */
    private double t_req_est;
    /**
     * 响应状态
     */
    private String t_resp_status;
    /**
     * 响应错误原因
     */
    private String t_resp_error;
    /**
     * 请求数据
     */
    private String t_req_data;
    /**
     * 响应数据
     */
    private String t_resp_data;
    /**
     * 请求字节数
     */
    private Long t_req_bytes;
    /**
     * 响应字节数
     */
    private Long t_resp_bytes;
    /**
     * 设备信息
     */
    private Initinfo initinfo;
    /**
     * 信息类型
     */
    private String t_infoType;

    public String getT_app_id() {
        return t_app_id;
    }

    public void setT_app_id(String t_app_id) {
        this.t_app_id = t_app_id;
    }

    public String getT_dev_id() {
        return t_dev_id;
    }

    public void setT_dev_id(String t_dev_id) {
        this.t_dev_id = t_dev_id;
    }

    public String getT_srv_url() {
        return t_srv_url;
    }

    public void setT_srv_url(String t_srv_url) {
        this.t_srv_url = t_srv_url;
    }

    public String getT_srv_name() {
        return t_srv_name;
    }

    public void setT_srv_name(String t_srv_name) {
        this.t_srv_name = t_srv_name;
    }

    public Date getT_req_time() {
        return t_req_time;
    }

    public void setT_req_time(Date t_req_time) {
        this.t_req_time = t_req_time;
    }

    public double getT_req_est() {
        return t_req_est;
    }

    public void setT_req_est(double t_req_est) {
        this.t_req_est = t_req_est;
    }

    public String getT_resp_status() {
        return t_resp_status;
    }

    public void setT_resp_status(String t_resp_status) {
        this.t_resp_status = t_resp_status;
    }

    public String getT_resp_error() {
        return t_resp_error;
    }

    public void setT_resp_error(String t_resp_error) {
        this.t_resp_error = t_resp_error;
    }

    public String getT_req_data() {
        return t_req_data;
    }

    public void setT_req_data(String t_req_data) {
        this.t_req_data = t_req_data;
    }

    public String getT_resp_data() {
        return t_resp_data;
    }

    public void setT_resp_data(String t_resp_data) {
        this.t_resp_data = t_resp_data;
    }

    public Long getT_req_bytes() {
        return t_req_bytes;
    }

    public void setT_req_bytes(Long t_req_bytes) {
        this.t_req_bytes = t_req_bytes;
    }

    public Long getT_resp_bytes() {
        return t_resp_bytes;
    }

    public void setT_resp_bytes(Long t_resp_bytes) {
        this.t_resp_bytes = t_resp_bytes;
    }

    public Initinfo getInitinfo() {
        return initinfo;
    }

    public void setInitinfo(Initinfo initinfo) {
        this.initinfo = initinfo;
    }

    public String getT_infoType() {
        return t_infoType;
    }

    public void setT_infoType(String t_infoType) {
        this.t_infoType = t_infoType;
    }
}

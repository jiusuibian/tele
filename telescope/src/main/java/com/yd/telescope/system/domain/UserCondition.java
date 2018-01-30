package com.yd.telescope.system.domain;

import com.yd.telescope.common.dto.DatatableReq;

public class UserCondition extends DatatableReq{

    private String create_start_time;
    private String create_end_time;
    private String status;
    private String username;

    public String getCreate_start_time() {
        return create_start_time;
    }

    public void setCreate_start_time(String create_start_time) {
        this.create_start_time = create_start_time;
    }

    public String getCreate_end_time() {
        return create_end_time;
    }

    public void setCreate_end_time(String create_end_time) {
        this.create_end_time = create_end_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

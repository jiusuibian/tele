package com.yd.telescope.stats.controller.respose;

import com.yd.telescope.stats.controller.dto.CrashStap;
import com.yd.telescope.stats.controller.dto.InitInfo;

import java.util.List;

public class BugDetailRep {

    private List<CrashStap> crashStapList;

    private InitInfo initInfo;

    private String t_stackinfo;

    public List<CrashStap> getCrashStapList() {
        return crashStapList;
    }

    public void setCrashStapList(List<CrashStap> crashStapList) {
        this.crashStapList = crashStapList;
    }

    public InitInfo getInitInfo() {
        return initInfo;
    }

    public void setInitInfo(InitInfo initInfo) {
        this.initInfo = initInfo;
    }

    public String getT_stackinfo() {
        return t_stackinfo;
    }

    public void setT_stackinfo(String t_stackinfo) {
        this.t_stackinfo = t_stackinfo;
    }
}

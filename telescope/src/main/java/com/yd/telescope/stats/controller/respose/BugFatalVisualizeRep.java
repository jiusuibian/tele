package com.yd.telescope.stats.controller.respose;

import java.util.List;
import java.util.Map;

public class BugFatalVisualizeRep {
    private List<Map<String, Object>> dataList;

    private List<String> fieldList;

    public List<Map<String, Object>> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    public List<String> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<String> fieldList) {
        this.fieldList = fieldList;
    }
}

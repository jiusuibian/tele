package com.yd.telescope.util;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class utilTest {

    @Test
    public void test(){
        System.out.println(5/20d);
    }

    @Test
    public void test2(){
        Map<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");
        map.put("d", "4");
        map.put("e", "5");
        System.out.println(JSONObject.toJSON(map));
    }
}

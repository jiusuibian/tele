package com.yd.telescope.common.interpretor;

import com.yd.telescope.system.domain.User;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class DummySessionInterpretor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = new User();
        user.setUserdesc("test");
        user.setUsername("测试员");
        user.setUserdesc("xxx部门组员");
        request.getSession().setAttribute("userinfo",user);

        Map<String,String> durations = new HashMap<>();
        durations.put("1m","1分钟");
        durations.put("30m","30分钟");
        durations.put("1h","1小时");
        durations.put("12h","12小时");
        durations.put("1d","1天");

        request.getSession().setAttribute("durations",durations);
        request.getSession().setAttribute("appType","mobile");
        return true;
    }
}

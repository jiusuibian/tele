package com.yd.telescopeapi.filter;

import com.alibaba.fastjson.JSONObject;
import com.yd.telescopeapi.domain.BugInfo;
import com.yd.telescopeapi.enums.ResultEnums;
import com.yd.telescopeapi.service.AppService;
import com.yd.telescopeapi.util.Md5Utils;
import com.yd.telescopeapi.util.ResultUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import sun.misc.BASE64Encoder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Order(1)
@WebFilter(filterName = "telescopeApiFilter", urlPatterns = "/*")
public class TelescopeApiFilter implements Filter {
    private final static Logger logger = LoggerFactory.getLogger(TelescopeApiFilter.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AppService appService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpServletRequest requestWrapper = new MAPIHttpServletRequestWrapper(request);

        String uri = requestWrapper.getRequestURI();
        // 判断请求接口是否正确
        if (uri.endsWith("bugInfo") || uri.endsWith("callInfo") || uri.endsWith("initInfo") || uri.endsWith("callInfo")) {
            boolean isCheck = check(requestWrapper);
            if (isCheck) {
                filterChain.doFilter(requestWrapper, response);
                return;
            }
        }
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().print(ResultUtils.error(ResultEnums.CHECK_POST_ERROR));
    }

    private boolean check(HttpServletRequest request) throws IOException {
        // 校验请求头信息是否完整
        String appId = request.getHeader("appId");
        String timestamp = request.getHeader("timestamp");
        String salt = request.getHeader("salt");
        String sign = request.getHeader("sign");

        // 判断参数是否为空
        if (StringUtils.isAnyBlank(appId, timestamp, salt, sign)) {
            return false;
        }

        // 判断是否重复请求
        if (!stringRedisTemplate.hasKey(salt + timestamp)) {
            return false;
        } else {
            stringRedisTemplate.opsForSet().add(salt + timestamp, salt + timestamp);
        }

        // 判断app状态
        Map<String, String> all = appService.getAppStatus();
        String s = all.get(appId);
        if (StringUtils.isBlank(s) || !s.equals("")) {
            return false;
        }

        // 校验签名是否正确
        Map<String, String> appKey = appService.getAppKey();
        if (!checkSign(request, appId + timestamp + salt + appKey.get(appId), sign)) {
            return false;
        }
        return true;
    }

    private boolean checkSign(HttpServletRequest request, String str, String sign) throws IOException {
        boolean flag = false;
        ServletInputStream inputStream = request.getInputStream();
        byte[] bytes = IOUtils.toByteArray(inputStream);
        BASE64Encoder base64Encoder = new BASE64Encoder();
        BugInfo parse = JSONObject.parseObject(bytes, BugInfo.class);

        if (sign.equals(Md5Utils.MD5Encode(str + base64Encoder.encode(bytes), "utf-8", false))) {
            flag = true;
        }
        return flag;
    }

    @Override
    public void destroy() {

    }

}

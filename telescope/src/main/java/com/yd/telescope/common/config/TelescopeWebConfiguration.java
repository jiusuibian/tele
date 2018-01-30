package com.yd.telescope.common.config;

import com.yd.telescope.common.interpretor.DummySessionInterpretor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class TelescopeWebConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new DummySessionInterpretor()).addPathPatterns("/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/system/user/view").setViewName("/system/user");
        registry.addViewController("/system/role/view").setViewName("/system/role");
        registry.addViewController("/system/menu/view").setViewName("/system/menu");
        registry.addViewController("/system/user/add/view").setViewName("/system/user-add");
        registry.addViewController("/system/role/add/view").setViewName("/system/role-add");
        registry.addViewController("/system/menu/add/view").setViewName("/system/menu-add");
        registry.addViewController("/system/icon/view").setViewName("/system/FontIcoList");
//        registry.addViewController("/app/update/view").setViewName("/management/app-update");
    }
}

package com.yd.telescope.common.config;

import com.yd.telescope.system.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import javax.servlet.http.HttpSession;

@Configuration
public class AuditorBean implements AuditorAware<String>{
    @Autowired
    private HttpSession session;
    @Override
    public String getCurrentAuditor() {
        User user = (User) session.getAttribute("userinfo");
        if(null != user){
            return user.getUsername();
        }
        return null;
    }
}

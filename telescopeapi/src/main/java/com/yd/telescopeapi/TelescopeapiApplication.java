package com.yd.telescopeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class TelescopeapiApplication {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext app = SpringApplication.run(TelescopeapiApplication.class, args);

    }
}

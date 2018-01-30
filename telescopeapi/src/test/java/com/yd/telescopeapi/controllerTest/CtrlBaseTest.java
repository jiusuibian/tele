package com.yd.telescopeapi.controllerTest;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Random;
import java.util.UUID;

/**
 * controller层基础测试类
 *
 * @author zygong
 * @create 2017-12-27 9:32
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class CtrlBaseTest {

    protected Random random = new Random();

    protected MockMvc mvc;

    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup(){
        this.mvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    protected String uuid(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    protected boolean randomBoolean(){
        return random.nextBoolean();
    }
}

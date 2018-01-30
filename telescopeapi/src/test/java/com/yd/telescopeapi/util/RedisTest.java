package com.yd.telescopeapi.util;

import com.yd.telescopeapi.TelescopeapiApplicationTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test(){
        // 保存字符串
        stringRedisTemplate.opsForValue().set("aaa", "", 1, TimeUnit.HOURS);
        Assert.assertEquals("", stringRedisTemplate.opsForValue().get("aaa"));
    }
    @Test
    public void test2(){
        if(stringRedisTemplate.hasKey("aaa")){
            Assert.assertTrue(true);
        }else{
            Assert.assertFalse(false);
        }
    }
}
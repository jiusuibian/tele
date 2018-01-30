package com.yd.telescope.stats.controller.util;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * @description: ES请求客户端
 * @author: zygong
 * @date: 2018/1/15 9:49
 */
@Component
public class ESClient implements CommandLineRunner{

    //    public static volatile RestHighLevelClient client;

    public static RestHighLevelClient client;

    @Override
    public void run(String... strings) throws Exception {
        client = new RestHighLevelClient(RestClient.builder(new HttpHost("10.19.105.60", 9200, "http")));
    }

    /**
     * @Description  在volatile 和synchronized 两端线程安全的保护下，保证了多线程访问情况下出现多个实例的问题。volatile 保证在实例对象创建后快速通知其他线程
     * @Anthor: zygong
     * @param :
     * @Return:
     * @Datatime: 2018/1/15 13:27
     */
//    public static RestHighLevelClient getInstance(){
//        if(client == null){
//            synchronized (ESClient.class){
//                if(client == null){
//                    client = new RestHighLevelClient(RestClient.builder(new HttpHost("10.19.105.60", 9200, "http")));
//                }
//            }
//        }
//        return client;
//    }


    public static void closeCliet() throws IOException {
        client.close();
    }

}

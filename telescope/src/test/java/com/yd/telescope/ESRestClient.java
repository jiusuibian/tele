package com.yd.telescope;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class ESRestClient {

    public static RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("10.19.105.60", 9200, "http")));

    public static void closeCliet() throws IOException {
        client.close();
    }

}

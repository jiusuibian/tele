package com.yd.telescope;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.yd.telescope.common.enums.ResultEnums;
import com.yd.telescope.common.exception.ESRestException;
import com.yd.telescope.stats.controller.dto.CrashStap;
import com.yd.telescope.stats.controller.dto.InitInfo;
import com.yd.telescope.stats.controller.respose.BugDetailRep;
import com.yd.telescope.stats.controller.util.ESClient;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.*;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.IncludeExclude;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.ParsedCardinality;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.IOException;
import java.sql.Time;
import java.util.*;

public class ESRestApiTest {

    @Test
    public void test() throws IOException {
        SearchRequest searchRequest = searchRequestInit();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);

        TermsAggregationBuilder agg = AggregationBuilders.terms("agg").field("initInfo.t_app_ver").order(BucketOrder.count(false)).size(20);
        RangeQueryBuilder epoch_millis = QueryBuilders.rangeQuery("@timestamp").gte(1515733832566l).lte(1515734732566l).format("epoch_millis");

        QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery("4.1.8").analyzeWildcard(false);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(epoch_millis).must(queryStringQueryBuilder);
        searchSourceBuilder.aggregation(agg);
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = ESRestClient.client.search(searchRequest);
        if(RestStatus.OK == searchResponse.status()){
            ParsedStringTerms parsedStringTerms = searchResponse.getAggregations().get("agg");
            long totalHits = searchResponse.getHits().getTotalHits();
            List<? extends Terms.Bucket> buckets = parsedStringTerms.getBuckets();
            for(Terms.Bucket bucket : buckets){
                System.out.println(bucket.getKeyAsString());
                System.out.println(bucket.getDocCount());
            }
        }

        ESRestClient.closeCliet();

    }

    @Test
    public void count() throws IOException {
        SearchRequest searchRequest = searchRequestInit();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);

//        TermsAggregationBuilder agg = AggregationBuilders.terms("agg").field("initInfo.t_app_ver").order(BucketOrder.count(false)).size(20);
        CardinalityAggregationBuilder bugAgg = AggregationBuilders.cardinality("bugAgg").field("t_stackinfo.hash");
        CardinalityAggregationBuilder userAgg = AggregationBuilders.cardinality("userAgg").field("initInfo.t_dev_id");
        RangeAggregationBuilder rangeAgg = AggregationBuilders.range("rangeAgg").field("t_status").addRange(0, 1);
        RangeQueryBuilder epoch_millis = QueryBuilders.rangeQuery("@timestamp").gte(1515686400000l).lte(1515772799999l).format("epoch_millis");

        searchSourceBuilder.aggregation(userAgg);
        searchSourceBuilder.aggregation(bugAgg);
        searchSourceBuilder.aggregation(rangeAgg);
        searchSourceBuilder.query(epoch_millis);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = ESRestClient.client.search(searchRequest);
        if(RestStatus.OK == searchResponse.status()){
            ParsedCardinality bugAggResult = searchResponse.getAggregations().get("bugAgg");
            ParsedCardinality userAggResult = searchResponse.getAggregations().get("userAgg");
            ParsedRange rangeAggResult = searchResponse.getAggregations().get("rangeAgg");

            System.out.println(bugAggResult.getValue());
            System.out.println(userAggResult.getValue());

            for(Range.Bucket bucket : rangeAggResult.getBuckets()){
                System.out.println(bucket.getDocCount());
            }
        }


        ESRestClient.closeCliet();
    }

    @Test
    public void bugListCount() throws IOException {
        SearchRequest searchRequest = searchRequestInit();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(5);
        searchSourceBuilder.from(0);

        String[] strArr = {"t_bug_attach", "t_bug_desc", "initInfo.t_app_ver", "t_time", "t_status"};
        searchSourceBuilder.fetchSource(strArr, null);

        CardinalityAggregationBuilder bugAgg = AggregationBuilders.cardinality("bugAgg").field("t_stackinfo.hash");
        RangeQueryBuilder epoch_millis = QueryBuilders.rangeQuery("@timestamp").gte(1515686400000l).lte(1515772799999l).format("epoch_millis");

        searchSourceBuilder.query(epoch_millis);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = ESRestClient.client.search(searchRequest);
        if(RestStatus.OK == searchResponse.status()){

            SearchHits searchHits = searchResponse.getHits();
            SearchHit[] hits = searchHits.getHits();
            for(SearchHit hit : hits){
                System.out.println(hit.getSourceAsString());
            }

        }

        ESRestClient.closeCliet();
    }

    @Test
    public void bugListScroll() throws IOException {
        final Scroll scroll = new Scroll(TimeValue.timeValueMillis(1l));

        SearchRequest searchRequest = searchRequestInit();
        searchRequest.scroll(scroll);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        String[] strArr = {"t_bug_attach", "t_bug_desc", "initInfo.t_app_ver", "t_time", "t_status"};
        searchSourceBuilder.fetchSource(strArr, null);

        RangeQueryBuilder epoch_millis = QueryBuilders.rangeQuery("@timestamp").gte(1515686400000l).lte(1515772799999l).format("epoch_millis");

        searchSourceBuilder.query(epoch_millis);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = ESRestClient.client.search(searchRequest);
        String scrollId = searchResponse.getScrollId();
        System.out.println(scrollId);
        if(RestStatus.OK == searchResponse.status()){

            SearchHits searchHits = searchResponse.getHits();
            SearchHit[] hits = searchHits.getHits();
            for(SearchHit hit : hits){
                System.out.println(hit.getSourceAsString());
            }

        }
        if(StringUtils.isNotBlank(scrollId)){
            scrollTest(scrollId);
        }

        ESRestClient.closeCliet();
    }

    private void scrollTest(String scrollId) throws IOException {
        SearchScrollRequest searchRequest = new SearchScrollRequest(scrollId);
        searchRequest.scroll(TimeValue.timeValueMillis(1l));

        SearchResponse searchResponse = ESRestClient.client.searchScroll(searchRequest);
        String scrollIdLast = searchResponse.getScrollId();
        System.out.println(scrollIdLast);
        if(RestStatus.OK == searchResponse.status()){

            SearchHits searchHits = searchResponse.getHits();
            SearchHit[] hits = searchHits.getHits();
            for(SearchHit hit : hits){
                System.out.println(hit.getSourceAsString());
            }

        }
        if(StringUtils.isNotBlank(scrollIdLast)){
            scrollTest(scrollIdLast);
        }
    }

    @Test
    public void bug_fatal_visualize_test() throws ESRestException {
        bug_fatal_visualize("initInfo.t_app_ver", mapData("116e204a50cf4246bc08de38586ca083", "7.8.8"), 5, 1515254400000l, 1515859199999l);
    }

    public void bug_fatal_visualize(String type, Map<String, Object> map, int size, long sTime, long eTime) throws ESRestException {
        List<String> list = new ArrayList<>();
        List<Map<String, Object>> listType = new ArrayList<>();
        Map<Long, Map<String, Object>> map2 = new TreeMap<>();
        try {
            SearchRequest searchRequest = searchRequestInit();

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);
            searchSourceBuilder.sort("@timestamp", SortOrder.DESC);

            TermsAggregationBuilder agg = AggregationBuilders.terms("agg").field(type).size(size).order(BucketOrder.count(false));
            DateHistogramAggregationBuilder agg2 = AggregationBuilders.dateHistogram("agg2").field("@timestamp").dateHistogramInterval(DateHistogramInterval.hours(3)).extendedBounds(new ExtendedBounds("", "now"));
            agg.subAggregation(agg2);

            // 查询条件
            RangeQueryBuilder epoch_millis = QueryBuilders.rangeQuery("@timestamp").gte(sTime).lte(eTime).format("epoch_millis");
//            QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(queryString(map)).analyzeWildcard(false);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(epoch_millis);
//            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(epoch_millis).must(queryStringQueryBuilder);
            searchSourceBuilder.query(boolQueryBuilder);

            searchSourceBuilder.aggregation(agg);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = ESRestClient.client.search(searchRequest);
            if (RestStatus.OK == searchResponse.status()) {
                ParsedStringTerms resultAgg = searchResponse.getAggregations().get("agg");
                Map<String, Object> map3 = null;
                for(Terms.Bucket bucket : resultAgg.getBuckets()){
                    String key = bucket.getKeyAsString();
                    ParsedDateHistogram agg21 = bucket.getAggregations().get("agg2");
                    for( Histogram.Bucket buckt2 : agg21.getBuckets()){
                        map3 = new HashMap<>();
                        if(!map2.containsKey(((DateTime)buckt2.getKey()).getMillis())){
                            map3.put("t_time", buckt2.getKeyAsString());
                            map3.put(bucket.getKeyAsString(), buckt2.getDocCount());
                            map2.put(((DateTime)buckt2.getKey()).getMillis(), map3);
                        }else{
                            Map<String, Object> stringLongMap = map2.get(((DateTime)buckt2.getKey()).getMillis());
                            stringLongMap.put(bucket.getKeyAsString(), buckt2.getDocCount());
                            map2.put(((DateTime)buckt2.getKey()).getMillis(), stringLongMap);
                        }
                    }
                }
            }
            for(Map.Entry<Long, Map<String, Object>> m : map2.entrySet()){
                listType.add(m.getValue());
            }
            System.out.println(JSONObject.toJSON(listType));
        } catch (IOException e) {
            throw new ESRestException(ResultEnums.ES_ERROR);
        }
    }

    @Test
    public void bugView() throws ESRestException {
        BugDetailRep rep = new BugDetailRep();
        try {
            GetRequest getRequest = new GetRequest("telescope-m116e204a50cf4246bc08de38586ca083-buginfo-2018.01.23", "doc", "oOP0IGEBjo6pRRVD6jNH");
            GetResponse getResponse = ESRestClient.client.get(getRequest);
            Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();

            rep.setInitInfo(JSON.parseObject(JSONObject.toJSONString(sourceAsMap.get("initInfo")), InitInfo.class));
            rep.setCrashStapList(JSON.parseArray(JSONObject.toJSONString(sourceAsMap.get("t_crash_steps")), CrashStap.class));
            rep.setT_stackinfo((String)sourceAsMap.get("t_stackinfo"));
            System.out.println(JSON.toJSONString(rep));
        } catch (IOException e) {
            throw new ESRestException(ResultEnums.ES_ERROR);
        }
    }

    public SearchRequest searchRequestInit(){
        SearchRequest searchRequest = new SearchRequest("telescope-m*-buginfo*");
        searchRequest.types("doc");
//        searchRequest.routing("routing");
        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());
        searchRequest.preference("_local");
        return searchRequest;
    }

    @Test
    public void clearScrollId() throws IOException {
        String scrollId = "DnF1ZXJ5VGhlbkZldGNobgAAAAAAAT-LFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_ihZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP_cWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT_sFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_jBZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP40WWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT-OFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_jxZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP5AWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT-RFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_khZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP5MWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT-UFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_lRZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP5YWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT-XFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_mBZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP5kWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT-aFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_mxZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP5wWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT-dFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_nhZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP58WWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT-gFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_oRZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP6IWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT-jFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_pBZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP6UWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT-mFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_pxZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP6gWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT-pFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_qhZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP6sWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT-sFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_rRZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP64WWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT-vFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_sBZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP7EWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT-yFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_sxZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP7QWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT-1FllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_thZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP7cWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT-4FllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_uRZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP7oWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT-7FllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_vBZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP70WWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT--FllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_vxZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP8AWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT_BFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_whZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP8MWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT_EFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_xRZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP8YWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT_HFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_yBZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP8kWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT_KFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_yxZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP8wWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT_NFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_zhZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP88WWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT_QFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_0RZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP9IWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT_TFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_1BZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP9UWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT_WFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_1xZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP9gWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT_ZFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_2hZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP9sWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT_cFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_3RZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP94WWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT_fFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_4BZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP-EWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT_iFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_4xZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP-QWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT_lFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_5hZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP-cWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT_oFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_6RZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP-";
        String scrollId2 = "oWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT_rFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_7RZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP-4WWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT_vFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_8BZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP_EWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT_yFllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_8xZZR0lKRG5WV1NuT0p5NFBOTVlWVkZBAAAAAAABP_QWWUdJSkRuVldTbk9KeTRQTk1ZVlZGQQAAAAAAAT_1FllHSUpEblZXU25PSnk0UE5NWVZWRkEAAAAAAAE_9hZZR0lKRG5WV1NuT0p5NFBOTVlWVkZB";
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId2);
        ClearScrollResponse clearScrollResponse = ESRestClient.client.clearScroll(clearScrollRequest);
        boolean succeeded = clearScrollResponse.isSucceeded();
        System.out.println(succeeded);
    }

    public Map<String, Object> mapData(String appid, String version) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(appid)) {
            map.put("initInfo.t_app_id", appid);
        }
        if (StringUtils.isNotBlank(version) && !"All".equals(version)) {
            map.put("initInfo.t_app_ver", version);
        }
        return map;
    }

    /**
     * @param map: 查询字段
     * @Description 查询字段拼接
     * @Anthor: zygong
     * @Return: java.lang.String
     * @Datatime: 2018/1/16 9:40
     */
    public String queryString(Map<String, Object> map) {
        String str = "";
        if (map == null || map.isEmpty()) {
            return null;
        }
        for (Map.Entry<String, Object> m : map.entrySet()) {
            str += m.getKey() + ":" + m.getValue() + " AND ";
        }
        str = str.substring(0, str.length() - 5);
        return str;
    }
}

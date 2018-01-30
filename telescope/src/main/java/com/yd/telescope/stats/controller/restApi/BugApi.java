package com.yd.telescope.stats.controller.restApi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yd.telescope.common.dto.DatatableRes;
import com.yd.telescope.common.enums.ResultEnums;
import com.yd.telescope.common.exception.ESRestException;
import com.yd.telescope.stats.controller.dto.CrashStap;
import com.yd.telescope.stats.controller.dto.InitInfo;
import com.yd.telescope.stats.controller.respose.BugDetailRep;
import com.yd.telescope.stats.controller.respose.BugFatalVisualizeRep;
import com.yd.telescope.stats.controller.util.ConstantUtil;
import com.yd.telescope.stats.controller.util.ESClient;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.*;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.ParsedCardinality;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;

/**
 * @description: bug查询类
 * @author: zygong
 * @date: 2018/1/15 9:46
 */
@Component
public class BugApi {

    /**
     * @param type:  崩溃汇总、版本分布、设备分布、系统分布
     * @param map:   查询条件
     * @param sTime: 开始时间
     * @param eTime: 结束时间
     * @Description 崩溃一览列表查询
     * @Anthor: zygong
     * @Return: java.util.Map<java.lang.String,java.lang.Object>
     * @Datatime: 2018/1/16 9:30
     */
    public Map<String, Object> bug_fatal_percent(String type, Map<String, Object> map, String sTime, String eTime) throws ESRestException {

        Map<String, Object> resultMap = new LinkedHashMap<>();
        try {
            SearchRequest searchRequest = searchRequestInit();

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);

            // 如果是崩溃汇总则不进行聚合统计
            if (!ConstantUtil.FATAL.equals(type)) {
                TermsAggregationBuilder agg = AggregationBuilders.terms("agg").field(type).order(BucketOrder.count(false)).size(20);
                searchSourceBuilder.aggregation(agg);
            }

            // 查询条件
            RangeQueryBuilder epoch_millis = QueryBuilders.rangeQuery("@timestamp").gte(sTime).lte(eTime).format("epoch_millis");
            QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(queryString(map)).analyzeWildcard(false);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(epoch_millis).must(queryStringQueryBuilder);
            searchSourceBuilder.query(boolQueryBuilder);

            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = ESClient.client.search(searchRequest);
            if (RestStatus.OK == searchResponse.status()) {
                // 如果是崩溃汇总则不进行聚合统计
                if (!ConstantUtil.FATAL.equals(type)) {
                    ParsedStringTerms parsedStringTerms = searchResponse.getAggregations().get("agg");
                    long totalHits = searchResponse.getHits().getTotalHits();
                    List<? extends Terms.Bucket> buckets = parsedStringTerms.getBuckets();
                    if (buckets == null || buckets.isEmpty()) {
                        return resultMap;
                    }
                    NumberFormat percentInstance = NumberFormat.getPercentInstance();
                    percentInstance.setMaximumFractionDigits(2);
                    for (Terms.Bucket bucket : buckets) {
                        resultMap.put(bucket.getKeyAsString(), percentInstance.format(bucket.getDocCount() / Double.valueOf(totalHits)));
                    }
                } else {
                    long totalHits = searchResponse.getHits().getTotalHits();
                    resultMap.put("全部", totalHits);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ESRestException(ResultEnums.ES_ERROR);
        }
        return resultMap;
    }

    /**
     * @param map:   查询条件
     * @param sTime: 开始时间
     * @param eTime: 结束时间
     * @Description 统计
     * @Anthor: zygong
     * @Return: java.util.Map<java.lang.String,java.lang.Object>
     * @Datatime: 2018/1/16 9:59
     */
    public Map<String, Object> count(Map<String, Object> map, String sTime, String eTime) throws ESRestException {

        Map<String, Object> resultMap = new HashMap<>();
        try {
            SearchRequest searchRequest = searchRequestInit();

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);

//            TermsAggregationBuilder agg = AggregationBuilders.terms("agg").field(type).order(BucketOrder.count(false)).size(20);
            CardinalityAggregationBuilder bugAgg = AggregationBuilders.cardinality("bugAgg").field("t_stackinfo.hash");
            CardinalityAggregationBuilder userAgg = AggregationBuilders.cardinality("userAgg").field("initInfo.t_dev_id");
            RangeAggregationBuilder rangeAgg = AggregationBuilders.range("rangeAgg").field("t_status").addRange(1, 2);

            // 查询条件
            RangeQueryBuilder epoch_millis = QueryBuilders.rangeQuery("@timestamp").gte(sTime).lte(eTime).format("epoch_millis");
            QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(queryString(map)).analyzeWildcard(false);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(epoch_millis).must(queryStringQueryBuilder);
            searchSourceBuilder.query(boolQueryBuilder);

            searchSourceBuilder.aggregation(userAgg);
            searchSourceBuilder.aggregation(bugAgg);
            searchSourceBuilder.aggregation(rangeAgg);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = ESClient.client.search(searchRequest);
            if (RestStatus.OK == searchResponse.status()) {
                long totalHits = searchResponse.getHits().getTotalHits();
                ParsedCardinality bugAggResult = searchResponse.getAggregations().get("bugAgg");
                ParsedCardinality userAggResult = searchResponse.getAggregations().get("userAgg");
                ParsedRange rangeAggResult = searchResponse.getAggregations().get("rangeAgg");

                resultMap.put("bugs", bugAggResult.getValue());
                resultMap.put("fatals", totalHits);
                resultMap.put("userimpacts", userAggResult.getValue());
                resultMap.put("solves", rangeAggResult.getBuckets().get(0).getDocCount());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ESRestException(ResultEnums.ES_ERROR);
        }
        return resultMap;
    }

    /**
     * @Description  bug列表
     * @Anthor: zygong
     * @param map: 查询条件
     * @param size: 分页size
     * @param from: 分页起始位置
     * @param sTime: 开始时间
     * @param eTime:  结束时间
     * @Return: com.yd.telescope.common.dto.DatatableRes<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     * @Datatime: 2018/1/22 10:42
     */
    public DatatableRes<List<Map<String, Object>>> bug_fatal_list(Map<String, Object> map, int size, int from, String sTime, String eTime) throws ESRestException {

        List<Map<String, Object>> list = new ArrayList<>();
        DatatableRes<List<Map<String, Object>>> pagingObject = new DatatableRes<>();
        try {
            SearchRequest searchRequest = searchRequestInit();

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(size);
            searchSourceBuilder.from(from);
            searchSourceBuilder.sort("t_time", SortOrder.DESC);

            String[] strArr = {"t_bug_attach", "t_bug_desc", "initInfo.t_app_ver", "t_time", "t_status"};
            searchSourceBuilder.fetchSource(strArr, null);

            // 查询条件
            RangeQueryBuilder epoch_millis = QueryBuilders.rangeQuery("@timestamp").gte(sTime).lte(eTime).format("epoch_millis");
            QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(queryString(map)).analyzeWildcard(false);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(epoch_millis).must(queryStringQueryBuilder);
            searchSourceBuilder.query(boolQueryBuilder);

            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = ESClient.client.search(searchRequest);
            if (RestStatus.OK == searchResponse.status()) {
                SearchHits searchHits = searchResponse.getHits();
                long totalHits = searchHits.getTotalHits();
                pagingObject.setRecordsFiltered(totalHits);
                pagingObject.setRecordsTotal(totalHits);
                SearchHit[] hits = searchHits.getHits();
                for (SearchHit hit : hits) {
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                    sourceAsMap.put("index", hit.getIndex());
                    sourceAsMap.put("id", hit.getId());
                    list.add(sourceAsMap);
                }
                pagingObject.setData(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ESRestException(ResultEnums.ES_ERROR);
        }
        return pagingObject;
    }

    /**
     * @Description  绘图数据
     * @Anthor: zygong
     * @param type: 统计维度：崩溃汇总、版本分布、设备分布、系统分布
     * @param map: 查询条件
     * @param size: 默认查询几条数据
     * @param duration:  时间区间
     * @Return: com.yd.telescope.stats.controller.respose.BugFatalVisualizeRep
     * @Datatime: 2018/1/22 10:43
     */
    public BugFatalVisualizeRep bug_fatal_visualize(String type, Map<String, Object> map, int size, String duration) throws ESRestException {

        BugFatalVisualizeRep rep = null;
        
        // 如果是崩溃汇总，则只进行时间聚合，其他的需要进行相对应的维度聚合基础上再进行时间聚合
        if(ConstantUtil.FATAL.equals(type)){
            rep = getFatalBugFatalVisualizeRep(map, size, duration);
        }else{
            rep = getOtherBugFatalVisualizeRep(type, map, size, duration);
        }
        return rep;
    }

    private BugFatalVisualizeRep getFatalBugFatalVisualizeRep(Map<String, Object> map, int size, String duration) throws ESRestException {
        BugFatalVisualizeRep rep = new BugFatalVisualizeRep();
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<String> fieldList = new ArrayList<>();
        try {
            SearchRequest searchRequest = searchRequestInit();

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);
            searchSourceBuilder.sort("@timestamp", SortOrder.DESC);

            DateHistogramAggregationBuilder timeAgg = AggregationBuilders.dateHistogram("timeAgg").field("@timestamp").dateHistogramInterval(mathDateHistogramInterval(duration)).extendedBounds(new ExtendedBounds("now-" + duration, "now"));

            // 查询条件
            RangeQueryBuilder epoch_millis = QueryBuilders.rangeQuery("@timestamp").gte("now-" + duration).lte("now").format("epoch_millis");
            QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(queryString(map)).analyzeWildcard(false);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(epoch_millis).must(queryStringQueryBuilder);
            searchSourceBuilder.query(boolQueryBuilder);
            searchSourceBuilder.aggregation(timeAgg);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = ESClient.client.search(searchRequest);
            if (RestStatus.OK == searchResponse.status()) {
                ParsedDateHistogram timeAggResult = searchResponse.getAggregations().get("timeAgg");
                Map<String, Object> timeMap = null;
                for (Histogram.Bucket histogramBucket : timeAggResult.getBuckets()) {
                    timeMap = new HashMap<>();
                    timeMap.put("t_time", histogramBucket.getKeyAsString());
                    timeMap.put("崩溃数", histogramBucket.getDocCount());
                    dataList.add(timeMap);
                }
            }
            fieldList.add("崩溃数");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ESRestException(ResultEnums.ES_ERROR);
        }
        rep.setDataList(dataList);
        rep.setFieldList(fieldList);
        return rep;
    }

    private BugFatalVisualizeRep getOtherBugFatalVisualizeRep(String type, Map<String, Object> map, int size, String duration) throws ESRestException {
        BugFatalVisualizeRep rep = new BugFatalVisualizeRep();
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<Object, Map<String, Object>> timeMap = new HashMap<>();
        List<String> fieldList = new ArrayList<>();
        try {
            SearchRequest searchRequest = searchRequestInit();

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);
            searchSourceBuilder.sort("@timestamp", SortOrder.DESC);

            TermsAggregationBuilder typeAgg = AggregationBuilders.terms("typeAgg").field(type).size(size).order(BucketOrder.count(false));
            DateHistogramAggregationBuilder timeAgg = AggregationBuilders.dateHistogram("timeAgg").field("@timestamp").dateHistogramInterval(mathDateHistogramInterval(duration)).extendedBounds(new ExtendedBounds("now-" + duration, "now"));
            typeAgg.subAggregation(timeAgg);

            // 查询条件
            RangeQueryBuilder epoch_millis = QueryBuilders.rangeQuery("@timestamp").gte("now-" + duration).lte("now").format("epoch_millis");
            QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery(queryString(map)).analyzeWildcard(false);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(epoch_millis).must(queryStringQueryBuilder);
            searchSourceBuilder.query(boolQueryBuilder);
            searchSourceBuilder.aggregation(typeAgg);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = ESClient.client.search(searchRequest);
            if (RestStatus.OK == searchResponse.status()) {
                ParsedStringTerms typeAggResult = searchResponse.getAggregations().get("typeAgg");
                Map<String, Object> bucketMap = null;
                for (Terms.Bucket termBucket : typeAggResult.getBuckets()) {
                    ParsedDateHistogram timeAggResult = termBucket.getAggregations().get("timeAgg");
                    fieldList.add(termBucket.getKeyAsString());
                    for (Histogram.Bucket histogramBucket : timeAggResult.getBuckets()) {
                        bucketMap = new HashMap<>();
                        if (!timeMap.containsKey((histogramBucket.getKey()))) {
                            bucketMap.put("t_time", histogramBucket.getKeyAsString());
                            bucketMap.put(termBucket.getKeyAsString(), histogramBucket.getDocCount());
                            timeMap.put(histogramBucket.getKey(), bucketMap);
                        } else {
                            Map<String, Object> stringLongMap = timeMap.get(histogramBucket.getKey());
                            stringLongMap.put(termBucket.getKeyAsString(), histogramBucket.getDocCount());
                            timeMap.put(histogramBucket.getKey(), stringLongMap);
                        }
                    }
                }
            }
            for (Map.Entry<Object, Map<String, Object>> m : timeMap.entrySet()) {
                dataList.add(m.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ESRestException(ResultEnums.ES_ERROR);
        }
        rep.setDataList(dataList);
        rep.setFieldList(fieldList);
        return rep;
    }

    public BugDetailRep bug_fatal_bugDetail(String index, String id) throws ESRestException {
        BugDetailRep rep = new BugDetailRep();
        try {
            GetRequest getRequest = new GetRequest(index, "doc", id);
            GetResponse getResponse = ESClient.client.get(getRequest);
            Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
            InitInfo initInfo = JSON.parseObject(JSONObject.toJSONString(sourceAsMap.get("initInfo")), InitInfo.class);
            initInfo.setT_cpu_usage((Double)sourceAsMap.get("t_cpu_usage"));
            initInfo.setT_free_mem((Double)sourceAsMap.get("t_free_mem"));
            initInfo.setT_free_space((Double)sourceAsMap.get("t_free_space"));
            initInfo.setT_free_power((Double)sourceAsMap.get("t_free_power"));
            initInfo.setT_telecom_opt((String)sourceAsMap.get("t_telecom_opt"));
            initInfo.setT_net_mode((String)sourceAsMap.get("t_net_mode"));
            initInfo.setT_ip((String)sourceAsMap.get("t_ip"));
            initInfo.setT_time((String)sourceAsMap.get("t_time"));
            initInfo.setT_net_speed((Double) sourceAsMap.get("t_net_speed"));
            rep.setInitInfo(initInfo);
            rep.setCrashStapList(JSON.parseArray(JSONObject.toJSONString(sourceAsMap.get("t_crash_steps")), CrashStap.class));
            rep.setT_stackinfo((String)sourceAsMap.get("t_stackinfo"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ESRestException(ResultEnums.ES_ERROR);
        }
        return rep;
    }

    /**
     * @Description  版本统计
     * @Anthor: zygong
     * @param appId:  应用id
     * @Return: java.util.List<java.lang.String>
     * @Datatime: 2018/1/22 10:46
     */
    public List<String> versionList(String appId) throws ESRestException {

        List<String> list = new ArrayList<>();
        try {
            SearchRequest searchRequest = searchRequestInit();

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);

            TermsAggregationBuilder versionListAgg = AggregationBuilders.terms("versionListAgg").field("initInfo.t_app_ver").size(100).order(BucketOrder.key(false));

            MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery("initInfo.t_app_id", appId);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(matchPhraseQueryBuilder);

            searchSourceBuilder.aggregation(versionListAgg);
            searchSourceBuilder.query(boolQueryBuilder);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = ESClient.client.search(searchRequest);
            if (RestStatus.OK == searchResponse.status()) {
                ParsedStringTerms resultAgg = searchResponse.getAggregations().get("versionListAgg");
                for (Terms.Bucket bucket : resultAgg.getBuckets()) {
                    list.add(bucket.getKeyAsString());
                }
            }
        } catch (IOException e) {
            throw new ESRestException(ResultEnums.ES_ERROR);
        }
        return list;
    }

    public DateHistogramInterval mathDateHistogramInterval(String duration) {
        DateHistogramInterval dateHistogramInterval = null;
        if (StringUtils.isNotBlank(duration)) {
            switch (duration) {
                case "1m":
                    dateHistogramInterval = DateHistogramInterval.seconds(2);
                    break;
                case "30m":
                    dateHistogramInterval = DateHistogramInterval.minutes(1);
                    break;
                case "1h":
                    dateHistogramInterval = DateHistogramInterval.minutes(2);
                    break;
                case "12h":
                    dateHistogramInterval = DateHistogramInterval.minutes(12);
                    break;
                case "1d":
                    dateHistogramInterval = DateHistogramInterval.hours(1);
                    break;
                default:
                    dateHistogramInterval = DateHistogramInterval.days(1);
            }
        }
        return dateHistogramInterval;
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

    /**
     * @param :
     * @Description searchRequestInit
     * @Anthor: zygong
     * @Return:
     * @Datatime: 2018/1/15 10:27
     */
    public SearchRequest searchRequestInit() {
        SearchRequest searchRequest = new SearchRequest("telescope-m*-buginfo*");
        searchRequest.types("doc");
//        searchRequest.routing("routing");
        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());
        searchRequest.preference("_local");
        return searchRequest;
    }
}

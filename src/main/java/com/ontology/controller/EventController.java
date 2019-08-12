package com.ontology.controller;

import com.ontology.bean.EsPage;
import com.ontology.bean.Result;
import com.ontology.controller.vo.PageQueryVo;
import com.ontology.controller.vo.QueryVo;
import com.ontology.utils.ElasticsearchUtil;
import com.ontology.utils.ErrorInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/event")
@CrossOrigin
public class EventController {

    private String indexName = "event_index";

    private String esType = "events";


    @ApiOperation(value = "分页查询数据", notes = "分页查询数据", httpMethod = "POST")
    @PostMapping
    public Result getPageData(@RequestBody PageQueryVo req) {
        String action = "queryData";
        Integer pageIndex = req.getPageIndex();
        Integer pageSize = req.getPageSize();
        String txHash = req.getTxHash();
        Integer blockHeight = req.getBlockHeight();
        String contractAddress = req.getContractAddress();
        List<QueryVo> queryParams = req.getEventParams();

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        int i = 0;
        for (QueryVo vo : queryParams) {
            if (StringUtils.isEmpty(vo.getText())) {
                i++;
                continue;
            }
            if (vo.getPercent() > 100) {
                vo.setPercent(100);
            } else if (vo.getPercent() < 0) {
                vo.setPercent(0);
            }
            MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("eventParam" + i, vo.getText()).minimumShouldMatch(vo.getPercent() + "%");
            boolQuery.must(queryBuilder);
            i++;
        }
        if (StringUtils.isNotEmpty(txHash)) {
            MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("txHash", txHash);
            boolQuery.must(queryBuilder);
        }
        if (blockHeight != null) {
            MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("blockHeight", blockHeight);
            boolQuery.must(queryBuilder);
        }
        if (StringUtils.isNotEmpty(contractAddress)) {
            MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("contractAddress", contractAddress);
            boolQuery.must(queryBuilder);
        }
        EsPage list = ElasticsearchUtil.searchDataPage(indexName, esType, pageIndex, pageSize, boolQuery, null, null, null);
        return new Result(action, ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.descEN(), list);
    }

    @ApiOperation(value = "根据id查询数据", notes = "根据id查询数据", httpMethod = "GET")
    @GetMapping("/api/v1/dataset/{id}")
    public Result getData(@PathVariable String id) {
        String action = "queryById";
        Map<String, Object> result = ElasticsearchUtil.searchDataById(indexName, esType, id, null);
        return new Result(action, ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.descEN(), result);
    }

}
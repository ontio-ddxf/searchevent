package com.ontology.controller;

import com.github.ontio.common.Helper;
import com.ontology.bean.EsPage;
import com.ontology.bean.Result;
import com.ontology.controller.vo.DataIdResp;
import com.ontology.controller.vo.PageQueryVo;
import com.ontology.controller.vo.QueryVo;
import com.ontology.controller.vo.TokenResp;
import com.ontology.utils.ElasticsearchUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dataId")
@CrossOrigin
public class DataIdController {

    private String indexName = "event_index";

    private String esType = "events";


    @ApiOperation(value = "查询dataId历史信息", notes = "查询dataId历史信息", httpMethod = "POST")
    @GetMapping("/{dataId}")
    public Result getPageData(@PathVariable String dataId) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        MatchQueryBuilder queryContract = QueryBuilders.matchQuery("contractAddress", "0300000000000000000000000000000000000000");
        MatchQueryBuilder queryName = QueryBuilders.matchQuery("eventParam0", "Register");
        MatchQueryBuilder queryOntid = QueryBuilders.matchQuery("eventParam1", dataId);
        boolQuery.must(queryContract);
        boolQuery.must(queryName);
        boolQuery.must(queryOntid);
        List<Map<String, Object>> list = ElasticsearchUtil.searchListData(indexName, esType, boolQuery, 1, "blockHeight,txHash,createdTime", null, null);
        Map<String, Object> map = list.get(0);
        int blockHeight = (int) map.get("blockHeight");
        String txHash = (String) map.get("txHash");
        String createdTime = (String) map.get("createdTime");

        BoolQueryBuilder queryToken = QueryBuilders.boolQuery();
        MatchQueryBuilder queryMintToken = QueryBuilders.matchQuery("eventParam0", "6d696e74546f6b656e");
        MatchQueryBuilder queryDataId = QueryBuilders.matchQuery("eventParam4", Helper.toHexString(dataId.getBytes()));
        MatchQueryBuilder queryMintContract = QueryBuilders.matchQuery("contractAddress", "3e7d3d82df5e1f951610ffa605af76846802fbae");
        queryToken.must(queryMintToken);
        queryToken.must(queryDataId);
        queryToken.must(queryMintContract);
        List<Map<String, Object>> tokenList = ElasticsearchUtil.searchListData(indexName, esType, queryToken, null, "eventParam6,eventParam8,txHash,createdTime,blockHeight", null, null);

        List<TokenResp> tokens = new ArrayList<>();
        for (Map<String, Object> token : tokenList) {
            long tokenId = Long.parseLong(Helper.reverse((String) token.get("eventParam6")), 16);
            TokenResp tokenResp = new TokenResp();
            tokenResp.setTokenId(tokenId);
            tokenResp.setBlockHeight((int) token.get("blockHeight"));
            tokenResp.setTxHash((String) token.get("txHash"));
            tokenResp.setCreatedTime((String) token.get("createdTime"));
            tokens.add(tokenResp);
        }
        DataIdResp resp = new DataIdResp();
        resp.setDataId(dataId);
        resp.setBlockHeight(blockHeight);
        resp.setTxHash(txHash);
        resp.setCreatedTime(createdTime);
        resp.setTokens(tokens);
        return new Result(0, "SUCCESS", resp);
    }

}
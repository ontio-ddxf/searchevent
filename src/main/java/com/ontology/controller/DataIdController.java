package com.ontology.controller;

import com.github.ontio.common.Helper;
import com.ontology.bean.Result;
import com.ontology.controller.vo.DataIdResp;
import com.ontology.controller.vo.TokenResp;
import com.ontology.utils.ConfigParam;
import com.ontology.utils.Constant;
import com.ontology.utils.ElasticsearchUtil;
import com.ontology.utils.ErrorInfo;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dataId")
@CrossOrigin
public class DataIdController {
    @Autowired
    private ConfigParam configParam;
    private static String searchDataId = "blockHeight,txHash,createdTime";
    private static String searchTokenId = "eventParam6,eventParam8,txHash,createdTime,blockHeight";

    @ApiOperation(value = "查询dataId历史信息", notes = "查询dataId历史信息", httpMethod = "POST")
    @GetMapping("/{dataId}")
    public Result queryDataIdHistory(@PathVariable String dataId) {
        String action = "queryDataIdHistory";

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        MatchQueryBuilder queryContract = QueryBuilders.matchQuery("contractAddress", configParam.CONTRACT_HASH_ONTID);
        MatchQueryBuilder queryName = QueryBuilders.matchQuery("eventParam0", "Register");
        MatchQueryBuilder queryOntid = QueryBuilders.matchQuery("eventParam1", dataId);
        boolQuery.must(queryContract);
        boolQuery.must(queryName);
        boolQuery.must(queryOntid);
        List<Map<String, Object>> list = ElasticsearchUtil.searchListData(Constant.ES_INDEX_EVENT, Constant.ES_TYPE_EVENT, boolQuery, 1, searchDataId, null, null);
        Map<String, Object> map = list.get(0);
        int blockHeight = (int) map.get(Constant.ES_BLOCK_HEIGHT);
        String txHash = (String) map.get(Constant.ES_TX_HASH);
        String createdTime = (String) map.get(Constant.ES_CREATED_TIME);

        BoolQueryBuilder queryToken = QueryBuilders.boolQuery();
        MatchQueryBuilder queryMintToken = QueryBuilders.matchQuery("eventParam0", configParam.CONTRACT_METHOD_MINTTOKEN);
        MatchQueryBuilder queryDataId = QueryBuilders.matchQuery("eventParam4", Helper.toHexString(dataId.getBytes()));
        MatchQueryBuilder queryMintContract = QueryBuilders.matchQuery("contractAddress", configParam.CONTRACT_HASH_DTOKEN);
        queryToken.must(queryMintToken);
        queryToken.must(queryDataId);
        queryToken.must(queryMintContract);
        List<Map<String, Object>> tokenList = ElasticsearchUtil.searchListData(Constant.ES_INDEX_EVENT, Constant.ES_TYPE_EVENT, queryToken, null, searchTokenId, null, null);

        List<TokenResp> tokens = new ArrayList<>();
        for (Map<String, Object> token : tokenList) {
            long tokenId = Long.parseLong(Helper.reverse((String) token.get("eventParam6")), 16);
            TokenResp tokenResp = new TokenResp();
            tokenResp.setTokenId(tokenId);
            tokenResp.setBlockHeight((int) token.get(Constant.ES_BLOCK_HEIGHT));
            tokenResp.setTxHash((String) token.get(Constant.ES_TX_HASH));
            tokenResp.setCreatedTime((String) token.get(Constant.ES_CREATED_TIME));
            tokens.add(tokenResp);
        }
        DataIdResp resp = new DataIdResp();
        resp.setDataId(dataId);
        resp.setBlockHeight(blockHeight);
        resp.setTxHash(txHash);
        resp.setCreatedTime(createdTime);
        resp.setTokens(tokens);
        return new Result(action, ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.descEN(), resp);
    }

}
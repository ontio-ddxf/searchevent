package com.ontology.controller;

import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.ontology.bean.Result;
import com.ontology.controller.vo.TokenDetailResp;
import com.ontology.controller.vo.TokenTransferResp;
import com.ontology.utils.ConfigParam;
import com.ontology.utils.Constant;
import com.ontology.utils.ElasticsearchUtil;
import com.ontology.utils.ErrorInfo;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tokenId")
@CrossOrigin
public class TokenIdController {
    @Autowired
    private ConfigParam configParam;
    private static String searchTokenId = "eventParam4,txHash,createdTime,blockHeight";
    private static String searchTokenTransfer = "eventParam1,eventParam2,blockHeight,createdTime,txHash";

    @ApiOperation(value = "查询tokenId历史信息", notes = "查询tokenId历史信息", httpMethod = "POST")
    @GetMapping("/{tokenId}")
    public Result queryTokenIdHistory(@PathVariable Long tokenId) {
        String action = "queryTokenIdHistory";

        String strTokenId = Long.toString(tokenId, 16);
        int transferTimes = 0;

        BoolQueryBuilder queryToken = QueryBuilders.boolQuery();
        MatchQueryBuilder queryMintToken = QueryBuilders.matchQuery("eventParam0", configParam.CONTRACT_METHOD_MINTTOKEN);
        MatchQueryBuilder queryTokenId = QueryBuilders.matchQuery("eventParam6", strTokenId);
        MatchQueryBuilder queryMintContract = QueryBuilders.matchQuery("contractAddress", configParam.CONTRACT_HASH_DTOKEN);
        queryToken.must(queryMintToken);
        queryToken.must(queryTokenId);
        queryToken.must(queryMintContract);

        List<Map<String, Object>> tokenList = ElasticsearchUtil.searchListData(Constant.ES_INDEX_EVENT, Constant.ES_TYPE_EVENT, queryToken, null, searchTokenId, null, null);
        Map<String, Object> token = tokenList.get(0);
        int blockHeight = (int) token.get(Constant.ES_BLOCK_HEIGHT);
        String txHash = (String) token.get(Constant.ES_TX_HASH);
        String createdTime = (String) token.get(Constant.ES_CREATED_TIME);
        String dataId = new String(Helper.hexToBytes((String) token.get("eventParam4")));

        BoolQueryBuilder queryTokenTransfer = QueryBuilders.boolQuery();
        MatchQueryBuilder queryTransfer = QueryBuilders.matchQuery("eventParam0", configParam.CONTRACT_METHOD_TRANSFER);
        MatchQueryBuilder queryTransferToken = QueryBuilders.matchQuery("eventParam3", strTokenId);
        MatchQueryBuilder queryTransferContract = QueryBuilders.matchQuery("contractAddress", configParam.CONTRACT_HASH_DTOKEN);
        queryTokenTransfer.must(queryTransfer);
        queryTokenTransfer.must(queryTransferToken);
        queryTokenTransfer.must(queryTransferContract);

        List<Map<String, Object>> transferResult = ElasticsearchUtil.searchListData(Constant.ES_INDEX_EVENT, Constant.ES_TYPE_EVENT, queryTokenTransfer, null, searchTokenTransfer, "createdTime.keyword", null);
        List<TokenTransferResp> tokenTransferResps = new ArrayList<>();
        if (!CollectionUtils.isEmpty(transferResult)) {
            transferTimes = transferResult.size();
            for (Map<String, Object> result : transferResult) {
                TokenTransferResp tokenTransferResp = new TokenTransferResp();
                String fromAddress = Address.parse((String) result.get("eventParam1")).toBase58();
                String toAddress = Address.parse((String) result.get("eventParam2")).toBase58();
                tokenTransferResp.setFromAddress(fromAddress);
                tokenTransferResp.setToAddress(toAddress);
                tokenTransferResp.setBlockHeight((int) result.get(Constant.ES_BLOCK_HEIGHT));
                tokenTransferResp.setTxHash((String) result.get(Constant.ES_TX_HASH));
                tokenTransferResp.setCreatedTime((String) result.get(Constant.ES_CREATED_TIME));
                tokenTransferResps.add(tokenTransferResp);
            }
        }
        TokenDetailResp tokenResp = new TokenDetailResp();
        tokenResp.setTokenId(tokenId);
        tokenResp.setDataId(dataId);
        tokenResp.setBlockHeight(blockHeight);
        tokenResp.setTxHash(txHash);
        tokenResp.setCreatedTime(createdTime);
        tokenResp.setTransferTimes(transferTimes);
        tokenResp.setTokenTransferResp(tokenTransferResps);

        return new Result(action, ErrorInfo.SUCCESS.code(), ErrorInfo.SUCCESS.descEN(), tokenResp);
    }

}
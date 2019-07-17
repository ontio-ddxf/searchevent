package com.ontology.controller;

import com.github.ontio.common.Address;
import com.github.ontio.common.Helper;
import com.ontology.bean.Result;
import com.ontology.controller.vo.DataIdResp;
import com.ontology.controller.vo.TokenDetailResp;
import com.ontology.controller.vo.TokenResp;
import com.ontology.controller.vo.TokenTransferResp;
import com.ontology.utils.ElasticsearchUtil;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tokenId")
@CrossOrigin
public class TokenIdController {

    private String indexName = "event_index";

    private String esType = "events";


    @ApiOperation(value = "查询tokenId历史信息", notes = "查询tokenId历史信息", httpMethod = "POST")
    @GetMapping("/{tokenId}")
    public Result getPageData(@PathVariable Long tokenId) {
        String strTokenId = Long.toString(tokenId, 16);
        int transferTimes = 0;

        BoolQueryBuilder queryToken = QueryBuilders.boolQuery();
        MatchQueryBuilder queryMintToken = QueryBuilders.matchQuery("eventParam0", "6d696e74546f6b656e");
        MatchQueryBuilder queryTokenId = QueryBuilders.matchQuery("eventParam6", strTokenId);
        MatchQueryBuilder queryMintContract = QueryBuilders.matchQuery("contractAddress", "3e7d3d82df5e1f951610ffa605af76846802fbae");
        queryToken.must(queryMintToken);
        queryToken.must(queryTokenId);
        queryToken.must(queryMintContract);

        List<Map<String, Object>> tokenList = ElasticsearchUtil.searchListData(indexName, esType, queryToken, null, "eventParam4,txHash,createdTime,blockHeight", null, null);
        Map<String, Object> token = tokenList.get(0);
        int blockHeight = (int) token.get("blockHeight");
        String txHash = (String) token.get("txHash");
        String createdTime = (String) token.get("createdTime");
        String dataId = new String(Helper.hexToBytes((String) token.get("eventParam4")));

        BoolQueryBuilder queryTokenTransfer = QueryBuilders.boolQuery();
        MatchQueryBuilder queryTransfer = QueryBuilders.matchQuery("eventParam0", "7472616e73666572");
        MatchQueryBuilder queryTransferToken = QueryBuilders.matchQuery("eventParam3", strTokenId);
        MatchQueryBuilder queryTransferContract = QueryBuilders.matchQuery("contractAddress", "3e7d3d82df5e1f951610ffa605af76846802fbae");
        queryTokenTransfer.must(queryTransfer);
        queryTokenTransfer.must(queryTransferToken);
        queryTokenTransfer.must(queryTransferContract);

        List<Map<String, Object>> transferResult = ElasticsearchUtil.searchListData(indexName, esType, queryTokenTransfer, null, "eventParam1,eventParam2,blockHeight,createdTime,txHash", "createdTime.keyword", null);
        List<TokenTransferResp> tokenTransferResps = new ArrayList<>();
        if (!CollectionUtils.isEmpty(transferResult)) {
            transferTimes = transferResult.size();
            for (Map<String, Object> result : transferResult) {
                TokenTransferResp tokenTransferResp = new TokenTransferResp();
                String fromAddress = Address.parse((String) result.get("eventParam1")).toBase58();
                String toAddress = Address.parse((String) result.get("eventParam2")).toBase58();
                tokenTransferResp.setFromAddress(fromAddress);
                tokenTransferResp.setToAddress(toAddress);
                tokenTransferResp.setBlockHeight((int) result.get("blockHeight"));
                tokenTransferResp.setTxHash((String) result.get("txHash"));
                tokenTransferResp.setCreatedTime((String) result.get("createdTime"));
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

        return new Result(0, "SUCCESS", tokenResp);
    }

}
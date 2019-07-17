package com.ontology.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TokenDetailResp {
    @ApiModelProperty(name="tokenId",value = "tokenId")
    private long tokenId;
    @ApiModelProperty(name="blockHeight",value = "blockHeight")
    private int blockHeight;
    @ApiModelProperty(name="dataId",value = "dataId")
    private String dataId;
    @ApiModelProperty(name="txHash",value = "txHash")
    private String txHash;
    @ApiModelProperty(name="createdTime",value = "createdTime")
    private String createdTime;
    @ApiModelProperty(name="transferTimes",value = "transferTimes")
    private int transferTimes;
    @ApiModelProperty(name="tokenTransferResp",value = "tokenTransferResp")
    private List<TokenTransferResp> tokenTransferResp;
}

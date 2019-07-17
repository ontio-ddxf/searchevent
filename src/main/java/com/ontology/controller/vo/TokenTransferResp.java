package com.ontology.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TokenTransferResp {
    @ApiModelProperty(name="blockHeight",value = "blockHeight")
    private int blockHeight;
    @ApiModelProperty(name="fromAddress",value = "fromAddress")
    private String fromAddress;
    @ApiModelProperty(name="toAddress",value = "toAddress")
    private String toAddress;
    @ApiModelProperty(name="txHash",value = "txHash")
    private String txHash;
    @ApiModelProperty(name="createdTime",value = "createdTime")
    private String createdTime;
}

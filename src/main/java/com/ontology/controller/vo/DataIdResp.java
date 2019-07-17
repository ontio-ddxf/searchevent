package com.ontology.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DataIdResp {
    @ApiModelProperty(name="dataId",value = "dataId")
    private String dataId;
    @ApiModelProperty(name="blockHeight",value = "blockHeight")
    private int blockHeight;
    @ApiModelProperty(name="txHash",value = "txHash")
    private String txHash;
    @ApiModelProperty(name="createdTime",value = "createdTime")
    private String createdTime;
    @ApiModelProperty(name="tokens",value = "tokens")
    private List<TokenResp> tokens;
}

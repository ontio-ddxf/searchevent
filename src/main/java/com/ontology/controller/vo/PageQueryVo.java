package com.ontology.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PageQueryVo {
    @ApiModelProperty(name="pageIndex",value = "pageIndex")
    private Integer pageIndex;
    @ApiModelProperty(name="pageSize",value = "pageSize")
    private Integer pageSize;
    @ApiModelProperty(name="txHash",value = "txHash")
    private String txHash;
    @ApiModelProperty(name="height",value = "height")
    private Integer blockHeight;
    @ApiModelProperty(name="contractHash",value = "contractHash")
    private String contractAddress;
    @ApiModelProperty(name="eventParams",value = "eventParams")
    private List<QueryVo> eventParams;
}

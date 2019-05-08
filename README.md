# searchevent
search event from elasticsearch

- [接口规范](#接口规范)
	- [查询数据，分页返回](#查询数据，分页返回)

## 接口规范

### 查询数据，分页返回
```
url：/api/v1/event
method：POST
```
根据存储数据的属性以及匹配百分比查询并返回分页数据。

请求：

```source-json
{
	"pageIndex": 0,
	"pageSize": 10,
	"blockHeight": 1687438,
	"contractAddress": "0200000000000000000000000000000000000000",
	"eventParams": [{
			"percent": 100,
			"text": "transfer"
		},
		{
			"percent": 100,
			"text": "AMsze4xL658Q43mnhftiHmDgyBGqVhUXFw"
		}
	],
	"txHash": "ab5619be768177bd8ccc3d65f4473c8817fe36cd3e06a7c48504322943991364"
}

```

| Field Name | Type | Description |
|---|---|---|
|pageIndex|Integer|起始页|
|pageSize|Integer|每页数据条数|
|blockHeight|Integer|区块高度|
|contractAddress|String|合约地址|
|eventParams|List|事件参数|
|percent|Integer|匹配度|
|text|String|数据属性|
|txHash|String|交易hash|

响应：

```source-json
{
	"code": 0,
	"msg": "SUCCESS",
	"result": {
		"currentPage": 0,
		"pageSize": 10,
		"recordCount": 2,
		"recordList": [{
				"blockHeight": 1687438,
				"eventParam0": "transfer",
				"eventParam1": "AMsze4xL658Q43mnhftiHmDgyBGqVhUXFw",
				"eventParam2": "AVjnVkKmk6VniGLSdLnW7UfxmpGCoT8py5",
				"eventParam3": "100000000000",
				"contractAddress": "0200000000000000000000000000000000000000",
				"id": "4894B5E6484149CABD3389B45504F7B9",
				"event": "{\"GasConsumed\":10000000,\"Notify\":[{\"States\":[\"transfer\",\"AMsze4xL658Q43mnhftiHmDgyBGqVhUXFw\",\"AVjnVkKmk6VniGLSdLnW7UfxmpGCoT8py5\",100000000000],\"ContractAddress\":\"0200000000000000000000000000000000000000\"},{\"States\":[\"transfer\",\"AMsze4xL658Q43mnhftiHmDgyBGqVhUXFw\",\"AFmseVrdL9f9oyCzZefL9tG6UbviEH9ugK\",10000000],\"ContractAddress\":\"0200000000000000000000000000000000000000\"}],\"TxHash\":\"ab5619be768177bd8ccc3d65f4473c8817fe36cd3e06a7c48504322943991364\",\"State\":1}",
				"txHash": "ab5619be768177bd8ccc3d65f4473c8817fe36cd3e06a7c48504322943991364"
			},
			{
				"blockHeight": 1687438,
				"eventParam0": "transfer",
				"eventParam1": "AMsze4xL658Q43mnhftiHmDgyBGqVhUXFw",
				"eventParam2": "AFmseVrdL9f9oyCzZefL9tG6UbviEH9ugK",
				"eventParam3": "10000000",
				"contractAddress": "0200000000000000000000000000000000000000",
				"id": "42396EC8CE774871BFDC31F64F6EE991",
				"event": "{\"GasConsumed\":10000000,\"Notify\":[{\"States\":[\"transfer\",\"AMsze4xL658Q43mnhftiHmDgyBGqVhUXFw\",\"AVjnVkKmk6VniGLSdLnW7UfxmpGCoT8py5\",100000000000],\"ContractAddress\":\"0200000000000000000000000000000000000000\"},{\"States\":[\"transfer\",\"AMsze4xL658Q43mnhftiHmDgyBGqVhUXFw\",\"AFmseVrdL9f9oyCzZefL9tG6UbviEH9ugK\",10000000],\"ContractAddress\":\"0200000000000000000000000000000000000000\"}],\"TxHash\":\"ab5619be768177bd8ccc3d65f4473c8817fe36cd3e06a7c48504322943991364\",\"State\":1}",
				"txHash": "ab5619be768177bd8ccc3d65f4473c8817fe36cd3e06a7c48504322943991364"
			}
		],
		"pageCount": 1,
		"beginPageIndex": 1,
		"endPageIndex": 1
	}
}
```

| Field Name | Type | Description |
| -- | -- | -- |
| code | int | 错误码 |
| msg | String | 成功为SUCCESS，失败为错误描述 |
| result | Object | 返回分页数据 |
|recordList|List|返回数据的主体
|currentPage|int|当前页|
|pageSize|int|每页数据条数|
|recordCount|int|总数据条数|
|pageCount|int|总页数|
|beginPageIndex|int|起始页|
|endPageIndex|int|结束页|

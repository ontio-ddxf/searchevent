package com.ontology.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service("ConfigParam")
public class ConfigParam {

	/**
	 *  合约地址
	 */

	@Value("${contract.hash.ontid}")
	public String CONTRACT_HASH_ONTID;

	@Value("${contract.hash.mp.auth}")
	public String CONTRACT_HASH_MP_AUTH;

	@Value("${contract.hash.mp}")
	public String CONTRACT_HASH_MP;

	@Value("${contract.hash.dtoken}")
	public String CONTRACT_HASH_DTOKEN;

	/**
	 *  合约方法名
	 */
	@Value("${contract.method.mintToken}")
	public String CONTRACT_METHOD_MINTTOKEN;

	@Value("${contract.method.transfer}")
	public String CONTRACT_METHOD_TRANSFER;

}
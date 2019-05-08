package com.ontology;

import com.ontology.utils.ElasticsearchUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void deleteIndex(){
		ElasticsearchUtil.deleteIndex("test_index");
	}

	@Test
	public void EnQuery(){
		Map<String,Object> obj = new HashMap<>();
		obj.put("blockHeight",1687439);
		obj.put("eventParam0","f40215d7fc698c74e8a015308d0d64fd8252e146 test");
		ElasticsearchUtil.addData(obj,"sync_index","events");
	}

}

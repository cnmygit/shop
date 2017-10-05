package com.taotao.search.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrCould {
	
	@Test
	//添加索引
	public void testSolrCloud() throws SolrServerException, IOException {
		//创建一个solrServer对象，CloudServer对象
		//参数：zkHost，字符串类型zookeeper的地址列表
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.133:2181,192.168.25.133:2182,192.168.25.133:2183") ;
		//指定默认collection
		solrServer.setDefaultCollection("collection2");
		//箱索引库中添加文档
		SolrInputDocument document = new SolrInputDocument() ;
		document.addField("id", "001");
		document.addField("item_title", "测试商品1");
		document.addField("item_price", "3214");
		solrServer.add(document) ;
		//提交
		solrServer.commit() ;
	}
	
	@Test
	//删除索引
	public void testDelIndex() throws SolrServerException, IOException {
		//创建一个solrServer对象，CloudServer对象
		//参数：zkHost，字符串类型zookeeper的地址列表
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.133:2181,192.168.25.133:2182,192.168.25.133:2183") ;
		//指定默认collection
		solrServer.setDefaultCollection("collection2");
		solrServer.deleteByQuery("*:*") ;
		solrServer.commit() ;
	}
}

package com.taotao.search.solr;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolr {
	
	@Test
	public void testSorlAndDocument() throws Exception {
		//创建一个solrServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr") ;
		//创建一个文档对象solrInputDocument
		SolrInputDocument document = new SolrInputDocument() ;
		//向文档中添加域
		document.addField("id", "test001");
		document.addField("item_title", "测试商品");
		document.addField("item_price", 100);
		//把文档写入索引库
		solrServer.add(document) ;
		//提交
		solrServer.commit() ;
	}
	
	@Test
	public void testQueryIndex() throws SolrServerException {
		//创建一个solrServer对象
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr") ;
		//创建一个query对象
		SolrQuery solrQuery = new SolrQuery() ;
		solrQuery.setQuery("一加") ;
		solrQuery.setStart(0) ;
		solrQuery.setRows(30) ;
		solrQuery.set("df", "item_title") ;
		solrQuery.setHighlight(true) ;
		solrQuery.setHighlightSimplePre("<em>") ;
		solrQuery.setHighlightSimplePost("</em>") ;
		//执行查询
		QueryResponse response = solrServer.query(solrQuery) ;
		//获得查询结果
		SolrDocumentList solrDocumentList = response.getResults() ;
		//查询结果记录数
		System.out.println("结果中的总记录数为：" + solrDocumentList.getNumFound());
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			String itemName = null ;
			//取高亮显示
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting() ;
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title") ;
			if(list != null && list.size() > 0) {
				itemName = list.get(0) ;
			}else {
				itemName = (String) solrDocument.get("item_title") ;
			}
			System.out.println(itemName);
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
		}
	}
}

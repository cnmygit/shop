package com.taotao.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;

@Repository
public class ItemSearchDao {
	@Autowired
	private SolrServer solrServer ;
	
	public SearchResult search(SolrQuery query) throws Exception {
		//根据query对象查询索引库
		QueryResponse response = solrServer.query(query) ;
		SearchResult result = new SearchResult() ;
		//取查询结果
		SolrDocumentList solrDocumentList = response.getResults() ;
		//取结果总记录数，并设置到searchResult
		result.setRecordCount(solrDocumentList.getNumFound());
		//取结果集
		List<SearchItem> itemList = new ArrayList<>() ;
		for (SolrDocument solrDocument : solrDocumentList) {
			SearchItem searchItem = new SearchItem() ;
			searchItem.setId(Long.parseLong(solrDocument.get("id").toString()));
			//取高亮显示
			String itemName = null ;
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting() ;
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title") ;
			if(list != null && list.size() > 0) {
				itemName = list.get(0) ;
			}else {
				itemName = (String) solrDocument.get("item_title") ;
			}
			searchItem.setTitle(itemName);
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			searchItem.setPrice((Long) solrDocument.get("item_price"));
			searchItem.setImage((String) solrDocument.get("item_image"));
			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
			//添加商品到列表
			itemList.add(searchItem) ;
		}
		result.setItemList(itemList);
		
		return result ;
	}
}

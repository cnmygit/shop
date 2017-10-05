package com.taotao.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.dao.ItemSearchDao;
import com.taotao.search.service.SearchService;

/**
 * 商品使用索引库搜索
 * @author Leo
 *
 */
@Service
public class SearchServiceImpl implements SearchService{

	@Autowired
	private ItemSearchDao itemSearchDao ;
	
	@Override
	public SearchResult search(String queryString, int page, int rows) throws Exception {
		//创建一个sqlQuery对象
		SolrQuery query = new SolrQuery() ;
		//设置查询条件
		query.setQuery(queryString) ;
		//设置分页条件
		if(page < 1) page = 1 ;
		query.setStart((page -1	) * rows) ;
		query.setRows(rows) ;
		//设置默认搜索域
		query.set("df", "item_title") ;
		//设置高亮
		query.setHighlight(true) ;
		query.setHighlightSimplePre("<em style=\"color:red\">") ;
		query.setHighlightSimplePost("</em>") ;
		//执行查询
		SearchResult searchResult = itemSearchDao.search(query) ;
		//计算总页数
		long pageCount = searchResult.getRecordCount() / rows ;
		if(searchResult.getRecordCount() % rows != 0) {
			pageCount++ ;
		}
		searchResult.setPageCount(pageCount);
		return searchResult;
	}

}

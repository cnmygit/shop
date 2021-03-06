package com.taotao.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.common.pojo.SearchItem;
import com.taotao.search.mapper.ItemMapper;

public class ItemAddListener implements MessageListener{
	
	@Autowired
	private ItemMapper ItemMapper ;
	@Autowired
	private SolrServer solrServer ;
	
	@Override
	public void onMessage(Message message) {
		//从消息中取出商品id
		try {
			TextMessage textMessage = (TextMessage) message ;
			String strItemId = textMessage.getText() ;
			//转换成Long
			Long itemId = new Long(strItemId) ;
			//根据商品id查询商品信息
			SearchItem searchItem = ItemMapper.getItemById(itemId) ;
			//把商品消息添加到索引库
			SolrInputDocument document = new SolrInputDocument() ;
			//向文档中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			document.addField("item_desc", searchItem.getItem_desc());
			//把文档写入索引库
			solrServer.add(document) ;
			//提交
			solrServer.commit() ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

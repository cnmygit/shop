package com.taotao.controller;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService ;
	@Autowired
	private JmsTemplate jmsTemplate ;
	@Resource(name="topicDestination")
	private Topic topicDestination ;
	
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		EasyUIDataGridResult result = itemService.getItemList(page, rows) ;
		return result ;
	}
	
	@RequestMapping("/item/save") 
	@ResponseBody
	public TaotaoResult addItem(TbItem tbItem, String desc) {
		//调动业务，添加商品
		TaotaoResult result = itemService.addItem(tbItem, desc) ;
		TbItem item = (TbItem) result.getData() ;
		final long itemId = item.getId() ;
		//商品添加完成后，发送mq消息
		jmsTemplate.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage("" + itemId) ;
				return textMessage;
			}
		});
		return result ;
	}
}

package com.taotao.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.common.pojo.SearchItem;
import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 监听商品添加事件，
 * 生成商品详情的静态页面
 * @author Leo
 *
 */
@Controller
public class HtmlGenListener implements MessageListener {

	@Autowired
	private ItemService itemService ;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer ;
	@Value("${HTML_OUT_PATH}")
	private String HTML_OUT_PATH ;
	
	@Override
	public void onMessage(Message message) {
		try {
			//创建一个MessageListener接口实现类
			//从Message中去商品id
			TextMessage textMessage = (TextMessage) message ;
			String strItemId = textMessage.getText() ;
			Long itemId = new Long(strItemId) ;
			//查询商品基本信息，
			TbItem tbItem = itemService.getItemById(itemId) ;
			//使用tbItem初始化Item对象
			Item item = new Item(tbItem) ;
			//根据商品id查询商品描述信息
			TbItemDesc tbItemDesc = itemService.getItemDescById(itemId) ;
			//创建数据集
			Map data = new HashMap<>() ;
			data.put("item", item) ;
			data.put("itemDesc", tbItemDesc) ;
			//创建商品详情页面的模版
			//指定文件输出目录
			Configuration configuration = freeMarkerConfigurer.createConfiguration() ;
			Template template = configuration.getTemplate("item.html") ;
			FileWriter writer = new FileWriter(new File(HTML_OUT_PATH + itemId + ".html")) ;
			//生成静态文件
			template.process(data, writer); 
			//关闭流
			writer.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

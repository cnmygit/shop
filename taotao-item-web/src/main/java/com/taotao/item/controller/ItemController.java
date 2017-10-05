package com.taotao.item.controller;

import java.awt.ItemSelectable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

/**
 * 商品详情页面展示
 * @author Leo
 *
 */
@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService ;
	
	@RequestMapping("/item/{itemId}")
	public String showItemInfo(@PathVariable Long itemId, Model model) {
		//根据商品id查询商品基本信息
		TbItem tbItem = itemService.getItemById(itemId) ;
		//使用tbItem初始化Item对象
		Item item = new Item(tbItem) ;
		//根据商品id查询商品描述信息
		TbItemDesc tbItemDesc = itemService.getItemDescById(itemId) ;
		//传递给页面信息
		model.addAttribute("item", item) ;
		model.addAttribute("itemDesc", tbItemDesc) ;
		//返回逻辑视图
		return "item" ;
	}
}

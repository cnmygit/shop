package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

public interface ItemService {
	EasyUIDataGridResult getItemList(int page, int rows) ;
	TaotaoResult addItem(TbItem tbItem, String desc) ;
	TbItem getItemById(long itemId) ;
	TbItemDesc getItemDescById(long itemId) ;
}

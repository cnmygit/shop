package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService{
	
	@Autowired
	private TbItemMapper itemMapper ;
	@Autowired
	private TbItemDescMapper itemDescMapper ;
	@Autowired
	private JedisClient jedisClient ;
	
	@Value("${REDIS_ITEM_KEY}")
	private String REDIS_ITEM_KEY ;
	@Value("${REDIS_ITEM_EXPIRE}")
	private Integer REDIS_ITEM_EXPIRE ;

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample() ;
		List<TbItem> list = itemMapper.selectByExample(example) ;
		//取分页信息
		PageInfo<TbItem> pageInfo = new PageInfo<>(list) ;
		
		//创建返回结果集对象
		EasyUIDataGridResult result = new EasyUIDataGridResult() ;
		result.setTotal(pageInfo.getTotal());
		result.setRows(list);
		return result ;
	}

	@Override
	public TaotaoResult addItem(TbItem tbItem, String desc) {
		//生成商品id
		long itemId = IDUtils.genItemId() ;
		tbItem.setId(itemId);
		//商品状态，1-正常，2-下架，3-删除
		tbItem.setStatus((byte) 1);
		Date date = new Date() ;
		tbItem.setCreated(date);
		tbItem.setUpdated(date); 
		//插入的商品表
		itemMapper.insert(tbItem) ;
		//商品描述
		TbItemDesc tbItemDesc = new TbItemDesc() ;
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setCreated(date);
		tbItemDesc.setUpdated(date);
		tbItemDesc.setItemDesc(desc); 
		//插入的商品描述表
		itemDescMapper.insert(tbItemDesc) ;
		return TaotaoResult.ok(tbItem);
	}

	@Override
	public TbItem getItemById(long itemId) {
		//先查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":BASE") ;
			if(StringUtils.isNotBlank(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class) ;
				return tbItem ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//缓存没有命中，查询数据库
		TbItem item = itemMapper.selectByPrimaryKey(itemId) ;
		//添加缓存
		try {
			jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":BASE", JsonUtils.objectToJson(item)) ;
			//设置过期时间
			jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":BASE", REDIS_ITEM_EXPIRE) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return item;
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		//先查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":DESC") ;
			if(StringUtils.isNotBlank(json)) {
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class) ;
				return itemDesc ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//缓存没有命中，查询数据库
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId) ;
		//添加缓存
		try {
			jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc)) ;
			//设置过期时间
			jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":DESC", REDIS_ITEM_EXPIRE) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return itemDesc;
	}

}

package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.jedis.JedisClient;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;

/**
 * 内容管理
 * @author Leo
 *
 */
@Service
public class ContentServiceImpl implements ContentService {
	@Autowired
	private TbContentMapper contentMapper ;
	@Autowired
	private JedisClient JedisClient ;
	
	@Value("${CONTENT_KEY}")
	private String CONTENT_KEY ;
	
	@Override
	public TaotaoResult insertContent(TbContent content) {
		//补全pojo属性
		content.setCreated(new Date());
		content.setUpdated(new Date());
		//将内容插入数据
		contentMapper.insert(content) ;
		//缓存同步
		JedisClient.hdel(CONTENT_KEY, content.getCategoryId().toString()) ;
		return TaotaoResult.ok();
	}

	@Override
	public List<TbContent> getContentList(long cid) {
		//先查询缓存
		try {
			//判断缓存是否命中
			String json = JedisClient.hget(CONTENT_KEY, cid + "") ;
			if(StringUtils.isNotBlank(json)) {
				//把json转化为list
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class) ;
				return list ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//根据内容类目id查询内容列表
		TbContentExample example = new TbContentExample() ;
		//设置查询条件
		Criteria criteria = example.createCriteria() ;
		criteria.andCategoryIdEqualTo(cid) ;
		//执行查询
		List<TbContent> list = contentMapper.selectByExample(example) ;
		//向缓存中保存结果
		try {
			JedisClient.hset(CONTENT_KEY, cid + "", JsonUtils.objectToJson(list)) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
}

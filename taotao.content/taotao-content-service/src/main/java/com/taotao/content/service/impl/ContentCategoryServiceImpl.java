package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;

/**
 * 内容分类管理
 * @author Leo
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper ;
	
	@Override
	public List<EasyUITreeNode> getContentCatList(long parentId) {
		//根据parentId查询子节点列表
		TbContentCategoryExample example = new TbContentCategoryExample() ;
		Criteria criteria = example.createCriteria() ;
		criteria.andParentIdEqualTo(parentId) ;
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example) ;
		//返回结果的list
		List<EasyUITreeNode> resultList = new ArrayList<>() ;
		for (TbContentCategory contentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode() ;
			node.setId(contentCategory.getId());
			node.setText(contentCategory.getName());
			node.setState(contentCategory.getIsParent()?"closed":"open");
			resultList.add(node) ;
		}
		return resultList;
	}

	@Override
	public TaotaoResult insetContentCat(long parentId, String name) {
		//创建一个分类对象
		TbContentCategory contentCategory = new TbContentCategory() ;
		contentCategory.setName(name);
		contentCategory.setParentId(parentId);
		//新添加的节点一定是叶子节点
		contentCategory.setIsParent(false);
		//排序方式默认是1
		contentCategory.setSortOrder(1);
		//正常1，删除2
		contentCategory.setStatus(1);
		Date date = new Date() ;
		contentCategory.setCreated(date);
		contentCategory.setUpdated(date);
		//插入节点
		contentCategoryMapper.insert(contentCategory) ;
		//判断父节点是否是叶子节点
		TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(parentId) ;
		if(!parentNode.getIsParent()) {
			//如果不是父节点，则将其更新为父节点
			parentNode.setIsParent(true);
			//更新父节点
			contentCategoryMapper.updateByPrimaryKey(parentNode) ;
		}
		return TaotaoResult.ok(contentCategory);
	}

	@Override
	public TaotaoResult updateContentCat(Long id, String name) {
		// 创建一个内容分类对象
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setName(name);
		contentCategory.setId(id);
		contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
		return TaotaoResult.ok(contentCategory);
	}

	@Override
	//有问题
	public TaotaoResult deleteContentCatById(Long id) {
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id) ;
		if(contentCategory.getIsParent()) {
			//判断当前节点是父节点，递归
			TbContentCategoryExample example = new  TbContentCategoryExample() ;
			Criteria criteria = example.createCriteria() ;
			criteria.andParentIdEqualTo(id) ;
			List<TbContentCategory> list = contentCategoryMapper.selectByExample(example) ;
			for (TbContentCategory subContentCategory : list) {
				deleteContentCatById(subContentCategory.getId()) ;
			}
		}else {
			//当前节点是叶子节点，删除
			contentCategoryMapper.deleteByPrimaryKey(id) ;
		}
		
//		//判断其父节点是否还有其它子节点，没有则更新父节点的状态
//		TbContentCategoryExample example = new TbContentCategoryExample() ;
//		Criteria criteria = example.createCriteria() ;
//		criteria.andParentIdEqualTo(parentId) ;
//		//找以要删除节点的父节点作为父节点的节点
//		List<TbContentCategory> selectByExample = contentCategoryMapper.selectByExample(example) ;
//		if(selectByExample == null) {
//			//如果为空更新要删除节点的父节点为叶子节点
//			TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(parentId) ;
//			parentNode.setIsParent(false);
//			//更新父节点
//			contentCategoryMapper.updateByPrimaryKey(parentNode) ;
//		}
		return TaotaoResult.ok();
	}

}

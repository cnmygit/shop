package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;

/**
 * 内容分类管理
 * @author Leo
 *
 */
@Controller
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService contentCategoryService ;
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(@RequestParam(defaultValue="0")Long id) {
		List<EasyUITreeNode> catList = contentCategoryService.getContentCatList(id) ;
		return catList ;
	}
	
	@RequestMapping("/content/category/create")
	@ResponseBody
	public TaotaoResult insetContentCat(Long parentId, String name) {
		TaotaoResult result = contentCategoryService.insetContentCat(parentId, name) ;
		return result ;
	}
	
	@RequestMapping("/content/category/update")
	@ResponseBody
	public TaotaoResult updateContentCat(Long id, String name) {
		TaotaoResult result = contentCategoryService.updateContentCat(id, name) ;
		return result ;
	}
	
	@RequestMapping("/content/category/delete/")
	@ResponseBody
	public TaotaoResult deleteContentCat(Long parentId, Long id) {
		TaotaoResult result = contentCategoryService.deleteContentCatById(id) ;
		return result ;
	}
}

package com.taotao.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.Ad1Node;

/**
 * 商城首页展示
 * @author Leo
 *
 */
@Controller
public class IndexController {
	
	@Autowired
	private ContentService contentService ;
	
	@Value("${AD1_CONTENT_CID}")
	private Long AD1_CONTENT_CID ;
	
	@Value("${AD1_WIDTH}")
	private Integer AD1_WIDTH ;
	
	@Value("${AD1_WIDTH_B}")
	private Integer AD1_WIDTH_B ;
	
	@Value("${AD1_HEIGTH}")
	private Integer AD1_HEIGTH ;
	
	@Value("${AD1_HEIGTH_B}")
	private Integer AD1_HEIGTH_B ;
	
	
	@RequestMapping("/index")
	public String showIndex(Model model) {
		//从属性文件中去内容id
		//根据内容id查询内容列表
		List<TbContent> contentList = contentService.getContentList(AD1_CONTENT_CID) ;
		List<Ad1Node> ad1List = new ArrayList<>() ;
		for (TbContent content : contentList) {
			Ad1Node node = new Ad1Node() ;
			node.setAlt(content.getSubTitle());
			node.setHref(content.getUrl());
			node.setSrc(content.getPic());
			node.setSrcB(content.getPic2());
			node.setWidth(AD1_WIDTH);
			node.setWidthB(AD1_WIDTH_B);
			node.setHeight(AD1_HEIGTH);
			node.setHeightB(AD1_HEIGTH_B);
			ad1List.add(node) ;
		}
		//将大广告位数据转换为json传回前台
		String ad1 = JsonUtils.objectToJson(ad1List) ;
		model.addAttribute("ad1", ad1) ;
		return "index" ;
	}
}

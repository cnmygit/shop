package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 商城页面
 * @author Leo
 *
 */
@Controller
public class PageController {
	
	@RequestMapping("/")
	public String showIndex() {
		return "index" ;
	}
	
	@RequestMapping("/{page}")
	public String showIndex(@PathVariable String page) {
		return page ;
	}
}

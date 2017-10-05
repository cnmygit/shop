package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登录、注册页面的展示
 * @author Leo
 *
 */
@Controller
public class PageController {
	
	@RequestMapping("/page/register")
	public String showRegister() {
		return "register" ;
	}
	
	@RequestMapping("/page/login")
	public String showLogin(String redirect, Model model) {
		model.addAttribute("redirect", redirect) ;
		return "login" ;
	}
}

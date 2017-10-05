package com.taotao.sso.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserLoginService;
import com.taotao.sso.service.UserRegisterService;

/**
 * 用户管理controller
 * @author Leo
 *
 */
@Controller
public class UserController {
	
	@Autowired
	private UserRegisterService userRegisterService ;
	@Autowired
	private UserLoginService userLoginService ;
	
	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY ;
	
	@RequestMapping(value="/user/check/{param}/{type}",method=RequestMethod.GET)
	@ResponseBody
	public TaotaoResult checkUserInfo(@PathVariable String param, @PathVariable Integer type) {
		TaotaoResult result = userRegisterService.checkUserInfo(param, type) ;
		return result ;
	}
	
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult registerUser(TbUser user) {
		TaotaoResult taotaoResult = userRegisterService.createUser(user) ;
		return taotaoResult ;
	}
	
	@RequestMapping(value="/user/login", method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult userLogin(String username, String password, 
			HttpServletRequest request, HttpServletResponse response) {
		TaotaoResult taotaoResult = userLoginService.login(username, password) ;
		
		//缺少用户名密码错误的代码
		
		//用户名密码正确，取token
		String token = taotaoResult.getData().toString() ;
		//设置cookie
		//cookie要跨域，cookie的有效期
		CookieUtils.setCookie(request, response, COOKIE_TOKEN_KEY, token);
		//返回结果
		return taotaoResult ;
	}
	
	@RequestMapping(value="/user/token/{token}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getUserByToken(@PathVariable String token, String callback) {
		TaotaoResult taotaoResult = userLoginService.getUserByToken(token) ;
		if(StringUtils.isNotBlank(callback)) {
			//客户端为jsonp请求，需要返回js代码
			String jsonResult = callback + "(" + JsonUtils.objectToJson(taotaoResult) + ");" ;
			return jsonResult ;
		}
		return JsonUtils.objectToJson(taotaoResult) ;
	}
}

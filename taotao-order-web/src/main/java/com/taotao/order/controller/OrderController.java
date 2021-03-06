package com.taotao.order.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;

/**
 * 订单确认页面
 * @author Leo
 *
 */
@Controller
public class OrderController {
	
	@Value("${COOKIE_CART_KEY}")
	private String COOKIE_CART_KEY ;
	
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request) {
		//取购物车商品列表
		List<TbItem> cartList = getCartList(request) ;
		//取用户id
		TbUser user = (TbUser) request.getAttribute("user") ;
		System.out.println(user.getUsername());
		//TODD
		//根据用户id查询收货地址列表，静态数据
		//从数据库中查询支付方式列表
		//传递给页面
		request.setAttribute("cartList", cartList);
		//返回逻辑视图
		return "order-cart" ;
		
	}
	
	
	//从cookie中去购物车列表
	private List<TbItem> getCartList(HttpServletRequest request) {
		//使用cookieUtils获取购物车列表
		String json = CookieUtils.getCookieValue(request, COOKIE_CART_KEY, true) ;
		//判断cookie中是否有值
		if(StringUtils.isBlank(json)) {
			//没有值，返回一个空的list
			return new ArrayList<>() ;
		}
		//把json转换成list
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class) ;
		return list ;
	}
}

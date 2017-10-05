package com.taotao.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserLoginService;

/**
 * 判断用户身份的拦截器
 * @author Leo
 *
 */
public class LoginInterceptor implements HandlerInterceptor {
	
	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY ;
	@Value("${SSO_URL}")
	private String SSO_URL ;
	
	@Autowired
	private UserLoginService userloginService ;
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e)
			throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView e)
			throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//从cookie中去token
		String token = CookieUtils.getCookieValue(request, COOKIE_TOKEN_KEY) ;
		//如果没有token，跳转到登录页面
		if(StringUtils.isBlank(token)) {
			//http://localhost:8088/page/login
			String url = SSO_URL + "/page/login?redirect=" + request.getRequestURL().toString() ;
			response.sendRedirect(url);
			//拦截
			return false ;
		}
		//如果有token，调用sso系统服务，根据token查询用户信息
		TaotaoResult result = userloginService.getUserByToken(token) ;
		TbUser user = null ;
		if(result != null && result.getStatus() == 200) {
			user = (TbUser) result.getData() ;
		}
		//如果查询不到用户信息，跳转到登录页面
		else {
			//http://localhost:8088/page/login
			String url = SSO_URL + "/page/login?redirect=" + request.getRequestURL().toString() ;
			response.sendRedirect(url);
			//拦截
			return false ;
		}
		//把user对象放到request中
		request.setAttribute("user", user);
		//如果查询到用户，放行
		return true;
	}

}

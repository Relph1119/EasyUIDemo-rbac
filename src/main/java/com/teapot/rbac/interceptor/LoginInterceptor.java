package com.teapot.rbac.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		//判断session中是否有用户信息
		if(session.getAttribute("user") == null) {
			if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {
                //ajax请求
                response.sendError(401);
            } else {
                response.sendRedirect("/");
            }
		}
		return true;
	}
	
}

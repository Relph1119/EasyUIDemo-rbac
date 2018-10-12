package com.teapot.rbac.interceptor;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import com.teapot.rbac.model.entity.User;

@Component
public class RightsInterceptor implements HandlerInterceptor{

	@Value("${system.super.user.id}")
	private Long superId;
	
	private PathMatcher mathcher = new AntPathMatcher();
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(user.getId() != superId) {
			Set<String> urls = (Set<String>) session.getAttribute("urls");
			String path = request.getServletPath();
			for (String url : urls) {
				if(mathcher.match(url, path)) {
					//能匹配到当前的url，表示已授权
					return true;
				}
			}
			if ("XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {
				// ajax请求
				response.sendError(403);
			} else {
				response.sendRedirect(request.getContextPath() + "/reject");
			}
			return false;
		}
		return true;
	}
	
}

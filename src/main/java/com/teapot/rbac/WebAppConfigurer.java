package com.teapot.rbac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.teapot.rbac.interceptor.LoginInterceptor;
import com.teapot.rbac.interceptor.RightsInterceptor;

@Configuration
public class WebAppConfigurer implements WebMvcConfigurer{
	
	@Autowired
	RightsInterceptor rightsInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor())
			.addPathPatterns("/**")
			.excludePathPatterns("/", "/login", "/error", "/static/**");
		
		//请求的权限过滤器
		registry.addInterceptor(rightsInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/", "/login", "/error","/reject", "/menus", "/logout","/css/**", "/js/**");
	}
	
}

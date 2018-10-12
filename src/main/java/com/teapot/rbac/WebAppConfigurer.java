package com.teapot.rbac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.teapot.rbac.interceptor.LoginInterceptor;
import com.teapot.rbac.interceptor.RightsInterceptor;

@Configuration
public class WebAppConfigurer implements WebMvcConfigurer{
	
	@Autowired
	RightsInterceptor rightsInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration registraion = registry.addInterceptor(new LoginInterceptor());
		//需要拦截程序的所有请求
		registraion.addPathPatterns("/**");
		//排除指定请求不拦截
		registraion.excludePathPatterns("/", "/login", "/error");
		
		//请求的权限过滤器
		InterceptorRegistration rightsRegistraion = registry.addInterceptor(rightsInterceptor);
		
		rightsRegistraion.addPathPatterns("/**");
		rightsRegistraion.excludePathPatterns("/", "/login", "/error","/reject", "/menus", "/js/*", "/css/*");
	}
	
	
}

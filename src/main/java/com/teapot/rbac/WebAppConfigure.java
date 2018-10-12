package com.teapot.rbac;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.teapot.rbac.interceptor.LoginInterceptor;

@Configuration
public class WebAppConfigure implements WebMvcConfigurer{
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration registraion = registry.addInterceptor(new LoginInterceptor());
		//需要拦截程序的所有请求
		registraion.addPathPatterns("/**");
		//排除指定请求不拦截
		registraion.excludePathPatterns("/", "/login", "/error");
	}
}

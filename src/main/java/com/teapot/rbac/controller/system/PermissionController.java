package com.teapot.rbac.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/system/permission")
@Slf4j
public class PermissionController {
	
	@RequestMapping
	public void index() {
		
	}
	
	@RequestMapping("/form")
	public void form() {
		
	}
}

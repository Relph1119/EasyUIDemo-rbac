package com.teapot.rbac.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/system/role")
@Slf4j
public class RoleController {
	
	@RequestMapping
	public void index() {
		
	}
}

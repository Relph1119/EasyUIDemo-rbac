package com.teapot.rbac.controller.system;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.teapot.rbac.common.JsonResult;
import com.teapot.rbac.model.dao.RoleDao;
import com.teapot.rbac.model.entity.Permission;
import com.teapot.rbac.model.entity.Role;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/system/role")
@Slf4j
public class RoleController {
	
	@Autowired
	private RoleDao roleDao;
	
	@RequestMapping
	public void index() {
		
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public Iterable list() {
		return roleDao.findAll();
	}
	
	@RequestMapping({"/form", "/load"})
	public String form(Long id, Model model) {
		if (id != null) {
			//编辑
			Role role = roleDao.findById(id).get();
			model.addAttribute("role", role);
		}
		return "/system/role/form";
	}
	
	@RequestMapping({"/save", "/update"})
	@ResponseBody
	@Transactional
	public JsonResult form(@Valid Role role, BindingResult br) {
		if(!br.hasErrors()) {
			roleDao.save(role);
			return JsonResult.success();
		} else {
			return JsonResult.error("校验不通过！");
		}
	}
	
	@GetMapping("/delete")
	@ResponseBody
	@Transactional
	public JsonResult delete(Long id) {
		if(roleDao.existsById(id)){
			roleDao.deleteById(id);
            return JsonResult.success();
		}
		return JsonResult.error("数据不存在！");
        
	}
}

package com.teapot.rbac.controller.system;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.teapot.rbac.common.JsonResult;
import com.teapot.rbac.model.dao.PermissionDao;
import com.teapot.rbac.model.entity.Permission;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/system/permission")
@Slf4j
@Transactional(readOnly=true)
public class PermissionController {
	
	@Autowired
	private PermissionDao permissionDao;
	
	@RequestMapping
	public void index() {
		
	}
	
	@RequestMapping({"/form", "/load"})
	public String form(Long id, Model model) {
		if (id != null) {
			//编辑
			Permission permission = permissionDao.findById(id).get();
			model.addAttribute("permission", permission);
		}
		return "/system/permission/form";
	}
	
	@PostMapping("/list")
	@ResponseBody
	public List<Permission> list(){
		List<Permission> roots = permissionDao.findAllByParentIsNull();
		this.revers(roots);
		return roots;
	}
	
	@PostMapping("/combo")
	@ResponseBody
	public List<Permission> combo(){
		List<Permission> roots = permissionDao.findAllByParentIsNull();
		this.revers(roots);
		return roots;
	}
	
	@RequestMapping({"/save", "/update"})
	@ResponseBody
	@Transactional
	public JsonResult form(@Valid Permission permission, BindingResult br) {
		if(!br.hasErrors()) {
			permissionDao.save(permission);
			return JsonResult.success();
		} else {
			return JsonResult.error("校验不通过！");
		}
	}
	
	@GetMapping("/delete")
	@ResponseBody
	@Transactional
	public JsonResult delete(Long id) {
		if(permissionDao.existsById(id)){
			permissionDao.deleteById(id);
            return JsonResult.success();
		}
		return JsonResult.error("数据不存在！");
        
	}
	
	/**
	 * 递归子节点
	 * @param nodes
	 */
	private void revers(List<Permission> nodes) {
		for (Permission root : nodes) {
			List<Permission> children = permissionDao.findAllByParent(root);
			this.revers(children);
			root.setChildren(children);
		}
	}
}

package com.teapot.rbac.controller.system;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.teapot.rbac.common.JsonResult;
import com.teapot.rbac.model.dao.UserDao;
import com.teapot.rbac.model.entity.User;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/system/user")
@Slf4j
public class UserController {
	
	@Autowired
	private UserDao userDao;
	
	@RequestMapping
	public void index() {
		
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public Map<String, Object> list(@RequestParam(defaultValue = "1")int page,@RequestParam(defaultValue = "10") int rows) {
		PageRequest pr = PageRequest.of(page - 1, rows);
		Page<User> pageData = userDao.findAll(pr);
		Map<String, Object> data = new HashMap<>();
		data.put("total", pageData.getTotalElements());
		data.put("rows", pageData.getContent());
		return data;
	}
	
	@RequestMapping({"/form", "/load"})
	public String form(Long id, Model model) {
		if (id != null) {
			//编辑
			User user = userDao.findById(id).get();
			model.addAttribute("user", user);
		}
		return "/system/user/form";
	}
	
	@RequestMapping({"/save", "/update"})
	@ResponseBody
	@Transactional
	public JsonResult form(@Valid User user, BindingResult br) {
		if(!br.hasErrors()) {
			userDao.save(user);
			return JsonResult.success();
		} else {
			return JsonResult.error("校验不通过！");
		}
	}
	
	@GetMapping("/delete")
	@ResponseBody
	@Transactional
	public JsonResult delete(Long id) {
		if(userDao.existsById(id)){
			userDao.deleteById(id);
            return JsonResult.success();
		}
		return JsonResult.error("数据不存在！");
        
	}
}

package com.teapot.rbac.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.teapot.rbac.model.dao.UserDao;
import com.teapot.rbac.model.entity.User;

@Controller
@RequestMapping("/")
public class AppController {
	
	@Autowired
	private UserDao userDao;
	
	@RequestMapping
	public String index(@SessionAttribute(value="user", required=false) User user) {
		if(user == null) {
			//此处表示未登录
			return "login";
		}
		//已登录
		return "index";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam String account,@RequestParam String password, HttpSession session, RedirectAttributes rda) {
		User user = userDao.findFirstByAccount(account);
		if(user != null && user.getEnable()) {
			if(user.getPassword().equals(DigestUtils.md5Hex(password))) {
				session.setAttribute("user", user);
			} else {
				rda.addFlashAttribute("error", "帐号和密码不匹配!");
			}
		} else {
			rda.addFlashAttribute("error", "帐号不可用!");
		}
		return "redirect:/";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}

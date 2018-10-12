package com.teapot.rbac.controller;

import java.security.Permissions;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.teapot.rbac.common.Menu;
import com.teapot.rbac.model.dao.PermissionDao;
import com.teapot.rbac.model.dao.UserDao;
import com.teapot.rbac.model.entity.Permission;
import com.teapot.rbac.model.entity.Role;
import com.teapot.rbac.model.entity.User;

@Controller
@RequestMapping("/")
public class AppController {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PermissionDao permissionDao;
	
	@Value("${system.super.user.id}")
	private Long superId;
	
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
				Set<Permission> permissions;
				
				//判断是否是超级管理员
				if(superId == user.getId()) {
					permissions = permissionDao.findAllByEnableOrderByWeightDesc(true);
				}else {
					//获取用户菜单
					Set<Role> roles = user.getRoles();
					permissions = new HashSet<>();
					roles.forEach(role -> {
						permissions.addAll(role.getPermissions());
					});
				}
				
				//存储菜单
				TreeSet<Permission> menus = new TreeSet<Permission>((o1, o2) -> {
					if(o1.getWeight() == o2.getWeight()) {
						return -1;
					} 
					return o1.getWeight() - o2.getWeight();
				});
				//存储权限key
				Set<String> keys = new HashSet<>();
				
				permissions.forEach(permission -> {
					if(permission.getEnable()) {
						if(permission.getType().equals(Permission.Type.MENU)) {
							//是菜单
							menus.add(permission);
						}
						//获取用户拥有的权限
						keys.add(permission.getPermissionKey());
					}
				});
				
				//树形数据转换
				List<Menu> menuList = new ArrayList<>();
				menus.forEach(permission -> {
					Menu m = new Menu();
					m.setId(permission.getId());
					m.setText(permission.getName());
					m.setParentId(permission.getParent() == null? null:permission.getParent().getId());
					m.setHref(permission.getPath());
					menuList.add(m);
				});
				
				session.setAttribute("user", user);
				session.setAttribute("menus", menuList);
				session.setAttribute("keys", keys);
				
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
	
	@RequestMapping("/menus")
	@ResponseBody
	public List<Menu> menus(@SessionAttribute("menus") List<Menu> menuList) {
		return menuList;
	}
}

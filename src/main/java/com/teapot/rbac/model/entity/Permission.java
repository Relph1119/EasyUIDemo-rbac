package com.teapot.rbac.model.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.boot.SpringApplication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Entity
@Table(name="rbac_permission")
@Data
public class Permission {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JsonIgnore
	private Permission parent;
	
	@Column(nullable=false)
	private String name;
	
	@Column(name="permission_key", nullable=false, length=32)
	private String permissionKey;
	
	/**
	 * 权限类型
	 */
	private String type;
	
	/**
	 * 菜单路径
	 */
	private String path;
	
	/**
	 * 资源
	 */
	private String resource;
	
	private Boolean enable = false;
	
	private String description;
	
	private Integer weight = 0;
	
	@OneToMany(cascade=CascadeType.REMOVE)
	@JoinColumn(name="parent_id", updatable=false)
	private List<Permission> children;
	
	@JsonProperty("text")
	public String getText() {
		return this.name;
	}
	
	/**
	 * 权限类型枚举
	 */
	public enum Type {
		MENU("菜单"),
		FUNCTION("功能"),
		BLOCK("区域");
		
		private String display;
		
		Type(String display) { this.display = display; }
		
		public String display() {
			return display;
		}
		
		public String toString() {
			return this.display + "[" + this.name() + "]";
		}
	}
}	
	

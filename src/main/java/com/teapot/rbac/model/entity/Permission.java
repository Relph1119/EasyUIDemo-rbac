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

import lombok.Data;

@Entity
@Table(name="rbac_permission")
@Data
public class Permission {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Permission parent;
	
	@Column(nullable=false)
	private String name;
	
	@Column(name="permission_key", nullable=false, length=32)
	private String permisionKey;
	
	private String type;
	
	private Boolean enable = false;
	
	private String description;
	
	private Integer wieght = 0;
	
	@OneToMany(cascade=CascadeType.REMOVE)
	@JoinColumn(name="parent_id", updatable=false)
	private List<Permission> children;
}	
	

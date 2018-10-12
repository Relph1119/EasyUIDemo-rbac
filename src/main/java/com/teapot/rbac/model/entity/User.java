package com.teapot.rbac.model.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name="rbac_user")
@Data
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique=true, length = 16, nullable=false)
	private String account;
	
	@Column(nullable=false, length=128)
	@JsonIgnore
	private String password;
	
	@Column(name="user_name", length=32)
	private String userName;
	
	private String tel;
	
	private Boolean enable = false;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
		name="rbac_user_role",
		joinColumns = @JoinColumn(name="user_id"),
		inverseJoinColumns = @JoinColumn(name="role_id")
	)
	private Set<Role> roles;
}

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
@Table(name="rbac_role")
@Data
public class Role {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false, length=32, unique=true)
	private String roleName;
	
	@Column(nullable=false, length=32, unique=true)
	private String roleKey;
	
	private Boolean enable = false;
	
	private String description;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name = "rbac_role_permission",
			joinColumns=@JoinColumn(name="role_id"),
			inverseJoinColumns = @JoinColumn(name="permission_id")
	)
	
	@JsonIgnore
	private Set<Permission> permissions;
}

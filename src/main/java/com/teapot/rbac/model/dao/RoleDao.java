package com.teapot.rbac.model.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.teapot.rbac.model.entity.Role;

@Repository
public interface RoleDao extends CrudRepository<Role, Long>{
	
}

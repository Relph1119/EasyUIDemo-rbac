package com.teapot.rbac.model.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.teapot.rbac.model.entity.Permission;

@Repository
public interface PermissionDao extends CrudRepository<Permission, Long>{
	
	/**
	 * 获取根节点
	 * @return
	 */
	List<Permission> findAllByParentIsNull();
	
	/**
	 * 根据父节点找子节点
	 * @param parent
	 * @return
	 */
	List<Permission> findAllByParent(Permission parent);

}

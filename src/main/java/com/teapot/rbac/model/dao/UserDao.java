package com.teapot.rbac.model.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.teapot.rbac.model.entity.User;

@Repository
public interface UserDao extends PagingAndSortingRepository<User, Long>{
	
}

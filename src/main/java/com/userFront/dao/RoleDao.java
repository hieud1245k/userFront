package com.userFront.dao;

import org.springframework.data.repository.CrudRepository;

import com.userFront.domain.security.Role;

public interface RoleDao extends CrudRepository<Role, Long> {
	Role findByName(String name);
}

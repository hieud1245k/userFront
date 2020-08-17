package com.userFront.seeder;

import org.springframework.stereotype.Component;

import com.userFront.dao.RoleDao;
import com.userFront.domain.security.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
@Component
public class DataSeedingListener implements ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	private RoleDao roleDao;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(roleDao.findByName("ROLE_USER")==null) {
			roleDao.save(new Role("ROLE_USER"));
		}
		if(roleDao.findByName("ROLE_ADMIN")==null) {
			roleDao.save(new Role("ROLE_ADMIN"));
		}
	}
}

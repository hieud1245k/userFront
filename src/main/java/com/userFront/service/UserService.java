package com.userFront.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.userFront.domain.User;
import com.userFront.domain.security.UserRole;
@Service
public interface UserService {
	User findByUsername(String username);
	User findByEmail(String email);
	boolean checkUserExists(String username,String email);
	boolean checkUsernameExists(String username);
	boolean checkEmailExists(String email);
	void save(User user);
	User createUser(User user, Set<UserRole> userRoles);
	User saveUser(User user);
}

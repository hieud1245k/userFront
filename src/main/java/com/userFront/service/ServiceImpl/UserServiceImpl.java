package com.userFront.service.ServiceImpl;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.userFront.dao.RoleDao;
import com.userFront.dao.UserDao;
import com.userFront.domain.User;
import com.userFront.domain.security.UserRole;
import com.userFront.service.AccountService;
import com.userFront.service.UserService;
@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private AccountService accountService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Override
	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	}

	@Override
	public boolean checkUserExists(String username, String email) {
		return checkUsernameExists(username)||checkEmailExists(email);
	}

	@Override
	public boolean checkUsernameExists(String username) {
		return findByUsername(username)!=null;
	}

	@Override
	public boolean checkEmailExists(String email) {
		return findByEmail(email)!=null;
	}

	@Override
	public void save(User user) {
		userDao.save(user);
	}
	@Override
	public User createUser(User user, Set<UserRole> userRoles) {
		User localUser = userDao.findByUsername(user.getUsername());
		if(localUser!=null) {
			LOG.info("User with username {} already exist. Nothing will be done. ",user.getUsername());
		} else {
			String encryptedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encryptedPassword);
			for(UserRole ur: userRoles) {
				roleDao.save(ur.getRole());
			}
			user.getUserRoles().addAll(userRoles);
			
			user.setPrimaryAccount(accountService.createPrimaryAccount());
			user.setSavingsAccount(accountService.createSavingsAccount());
			localUser = userDao.save(user);
		}
		return localUser;
	}

	@Override
	public User saveUser(User user) {
		return userDao.save(user);
	}
	
}

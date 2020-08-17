package com.userFront.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.userFront.domain.User;
import com.userFront.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@RequestMapping(value = "/profile",method = RequestMethod.GET)
	public String profile(Model model,Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		return "profile";
	}
	@RequestMapping(value = "profile",method = RequestMethod.POST)
	public String profilePOST(@ModelAttribute("user") User user,Model model) {
		User user2 = userService.findByUsername(user.getUsername());
		user2.setUsername(user.getUsername());
		user2.setFirstName(user.getFirstName());
		user2.setLastName(user.getLastName());
		user.setEmail(user.getEmail());
		user.setPhone(user.getPhone());
		model.addAttribute("user", user2);
		userService.saveUser(user2);
		return "profile";
	}
}

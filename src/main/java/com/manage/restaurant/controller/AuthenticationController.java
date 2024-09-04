package com.manage.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.manage.restaurant.dtos.SignupDto;
import com.manage.restaurant.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {

	@Autowired
	UserService service;

	@GetMapping(path = {"/restaurant/home","","/"})
	public String getHome() {
		return "loginHome";
	}
	@GetMapping(path = {"/restaurant/signin"})
	public String getSignin() {
		return "signin";
	}

	@GetMapping("/restaurant/signup")
	public String getSignup() {
		return "signup";
	}

	@PostMapping("/restaurant/signup")
	public String postSignup(@ModelAttribute SignupDto userdto, Model model) {
		if (service.usernameExists(userdto.getUsername())) {
			model.addAttribute("message", "User already exists");
			return "signup";
		} else {
			Boolean resp = service.saveUser(userdto);

			if (resp) {
				model.addAttribute("message", "Rigistered successful, please login!");
				return "signin";
			} else {
				model.addAttribute("message", "Something went wrong!");
				return "signup";
			}
		}
	}
	
	@GetMapping("/error")
	public String error() {
		return "error";
	}

}

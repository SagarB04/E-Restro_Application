package com.manage.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.manage.restaurant.dtos.SignupDto;
import com.manage.restaurant.entity.CustomUserDetails;
import com.manage.restaurant.entity.User;
import com.manage.restaurant.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository repo;
	
	@Autowired
	PasswordEncoder passwordencoder;

	public Boolean saveUser(SignupDto dto) {
		User user = new User(

				dto.getId(), 
				dto.getName(), 
				dto.getUsername(), 
				passwordencoder.encode(dto.getPassword()),
				dto.getRole()
		);

		try {
			repo.save(user);
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	public CustomUserDetails getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        
        return userDetails;
    }

	public boolean usernameExists(String username) {
        return repo.findByUsername(username).isPresent();
	}

}

package com.manage.restaurant.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.manage.restaurant.entity.CustomUserDetails;
import com.manage.restaurant.entity.User;
import com.manage.restaurant.repository.UserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> user = repo.findByUsername(username);

		if (user.isPresent()) {
			return new CustomUserDetails(user.get());
		} else {
			throw new UsernameNotFoundException(username);
		}
	}

}

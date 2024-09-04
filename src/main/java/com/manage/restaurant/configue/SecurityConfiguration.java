package com.manage.restaurant.configue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.manage.restaurant.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Bean
	UserDetailsService userDetailsService() {
		return userDetailsService;
	}
	
	@Bean
	AuthenticationProvider authenticationProvide() {
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
		
		return provider;
		
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
			    .authorizeHttpRequests(request -> 
			        request
				        .requestMatchers("/restaurant/admin/**").hasRole("ADMIN")
	                    .requestMatchers("/restaurant/customer/**").hasRole("CUSTOMER")
			            .requestMatchers("/restaurant/signin", "/restaurant/signup","/restaurant/home","/").permitAll()
			            .anyRequest().authenticated()
			    )
			    .formLogin(form -> 
			        form
			            .loginPage("/restaurant/signin")
			            .successHandler(customAuthenticationSuccessHandler) 
			            .permitAll()
			    )
			    .logout(logout -> 
			        logout
			        	.permitAll()
			        	.logoutSuccessUrl("/restaurant/home") 
			    )
			    .build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}

package com.manage.restaurant.dtos;

import com.manage.restaurant.entity.Roles;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class SignupDto {
	private int id;
	private String name;
	private String username;
	private String password;
	@Enumerated(EnumType.STRING)
	private Roles role;

	public SignupDto() {
		super();
	}

	public SignupDto(int id, String name, String username, String password, Roles role) {
		super();
		this.id = id;
		this.name = name;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public Roles getRole() {
		return role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

package com.iktakademija.ednevnik.entities.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class UserDTOPut {

	private String name;
	
	private String surname;
	
	@Email(message = "Email is not valid.")
	private String email;
	
	@Size(min = 5, max = 600, message = "Username must be between {min} and {max} characters long.")
	private String username;
	
	private String password;
	
	public UserDTOPut() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserDTOPut(String name, String surname, @Email(message = "Email is not valid.") String email,
			@Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.") String username,
			String password) {
		super();
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

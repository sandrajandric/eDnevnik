package com.iktakademija.ednevnik.entities.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserDTO {

	@NotBlank(message = "First name must not be left blank.")
	private String name;
	
	@NotBlank(message = "Last name must not be left blank.")
	private String surname;
	
	@Email(message = "Email is not valid.")
	private String email;
	
	@NotBlank(message = "Username must not be left blank.")
	@Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.")
	private String username;
	
	@NotBlank(message = "Password must not be left blank.")
	private String password;

	public UserDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserDTO(@NotBlank(message = "First name must not be left blank.") String name,
			@NotBlank(message = "Last name must not be left blank.") String surname,
			@Email(message = "Email is not valid.") String email,
			@NotBlank(message = "Username must not be left blank.") @Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.") String username,
			@NotBlank(message = "Password must not be left blank.") String password) {
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

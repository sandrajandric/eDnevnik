package com.iktakademija.ednevnik.entities.dto;

public class UserTokenDTO {

	private String username;
	private String token;
	
	public UserTokenDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UserTokenDTO(String username, String token) {
		super();
		this.username = username;
		this.token = token;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
}

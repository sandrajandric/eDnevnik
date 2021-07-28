package com.iktakademija.ednevnik.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.ednevnik.security.Views;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "user")
public class UserEntity {

	@JsonView(Views.Admin.class)
	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private Integer id;
	
	@JsonView(Views.Private.class)
	@Column(nullable = false)
	@NotBlank(message = "First name must not be left blank.")
	private String name;
	
	@JsonView(Views.Private.class)
	@Column(nullable = false)
	@NotBlank(message = "Last name must not be left blank.")
	private String surname;
	
	@JsonView(Views.Private.class)
	@Column(nullable = false, unique = true)
	@Email(message = "Email is not valid.")
	private String email;
	
	@JsonView(Views.Public.class)
	@Column(nullable = false, unique = true)
	@NotBlank(message = "Username must not be left blank.")
	@Size(min = 5, max = 20, message = "Username must be between {min} and {max} characters long.")
	private String username;
	
	@JsonView(Views.Public.class)
	@JsonIgnore
	@Column(nullable = false)
	@NotBlank(message = "Password must not be left blank.")
	private String password;
	
	@JsonView(Views.Admin.class)
	@JsonBackReference(value = "ru")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "role")
	private RoleEntity role;
	
	@JsonView(Views.Admin.class)
	@Version
	private Integer version;

	public UserEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Role: " + getRole()	+ ", Name: " + getName() + ", Surname: " + getSurname();
	}
}

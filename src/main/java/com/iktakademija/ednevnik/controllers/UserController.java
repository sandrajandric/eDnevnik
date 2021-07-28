package com.iktakademija.ednevnik.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.ednevnik.entities.UserEntity;
import com.iktakademija.ednevnik.repositories.UserRepository;
import com.iktakademija.ednevnik.security.Views;


@RestController
@RequestMapping(path = "/api/v1/users")
@Secured("ROLE_ADMIN")
public class UserController {

	@Value("${spring.security.secret-key}")
	private String secretKey;

	@Value("${spring.security.token-duration}")
	private Long tokenDuration;

	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> listUsers() {
		return new ResponseEntity<List<UserEntity>>((List<UserEntity>) userRepository.findAll(), HttpStatus.OK);
	}
	
	@RequestMapping(path = "/logs", method = RequestMethod.GET)
	@JsonView(Views.Admin.class)
	public ResponseEntity<?> readLogs() throws IOException {
		BufferedReader read = new BufferedReader(
				new FileReader("logs//spring-boot-logging.log"));
		
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = read.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		return new ResponseEntity<>(sb.toString(), HttpStatus.OK);
	}
}

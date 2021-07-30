package com.iktakademija.ednevnik.controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktakademija.ednevnik.controllers.util.RESTError;
import com.iktakademija.ednevnik.entities.ClassEntity;
import com.iktakademija.ednevnik.entities.UserEntity;
import com.iktakademija.ednevnik.repositories.UserRepository;

@RestController
@RequestMapping(path = "/api/v1/users")
@Secured("ROLE_ADMIN")
public class UserController {

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> listUsers() {
		List<UserEntity> users = new ArrayList<>();
		users = (List<UserEntity>) userRepository.findAll();
		if (!users.isEmpty()) {
			logger.info("Viewed all users.");
			return new ResponseEntity<List<UserEntity>>((List<UserEntity>) userRepository.findAll(), HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Users not found"),
					HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(path = "/logs", method = RequestMethod.GET)
	public ResponseEntity<?> getLogs() throws IOException {
		BufferedReader read = new BufferedReader(new FileReader("logs//spring-boot-logging.log"));

		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = read.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		return new ResponseEntity<>(sb.toString(), HttpStatus.OK);
	}
}

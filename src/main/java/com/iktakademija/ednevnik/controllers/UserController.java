package com.iktakademija.ednevnik.controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.ednevnik.entities.UserEntity;
import com.iktakademija.ednevnik.entities.dto.UserTokenDTO;
import com.iktakademija.ednevnik.repositories.UserRepository;
import com.iktakademija.ednevnik.security.Views;
import com.iktakademija.ednevnik.util.Encryption;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class UserController {

	@Value("${spring.security.secret-key}")
	private String secretKey;

	@Value("${spring.security.token-duration}")
	private Long tokenDuration;

	@Autowired
	private UserRepository userRepository;
	
	private String getJWTToken(UserEntity userEntity) {
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(userEntity.getRole().getName());
		String token = Jwts.builder().setId("softtekJWT").setSubject(userEntity.getEmail())
				.claim("authorities",
						grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + this.tokenDuration))
				.signWith(SignatureAlgorithm.HS512, this.secretKey.getBytes()).compact();
		return "Bearer " + token;
	}

	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestParam("user") String email, @RequestParam("password") String pwd) {
		UserEntity userEntity = userRepository.findByEmail(email);
		String encodedPassword = new BCryptPasswordEncoder().encode(userEntity.getPassword());
		
		if (userEntity != null && Encryption.validatePassword(pwd, encodedPassword)) {
			String token = getJWTToken(userEntity);
			UserTokenDTO user = new UserTokenDTO();
			user.setUsername(email);
			user.setToken(token);
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
		return new ResponseEntity<>("Wrong credentials", HttpStatus.UNAUTHORIZED);
	}

	@RequestMapping(path = "/api/v1/users", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> listUsers() {
		return new ResponseEntity<List<UserEntity>>((List<UserEntity>) userRepository.findAll(), HttpStatus.OK);
	}
	
	@RequestMapping(path = "/api/v1/logs", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
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

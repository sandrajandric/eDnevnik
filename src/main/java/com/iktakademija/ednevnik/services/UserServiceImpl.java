package com.iktakademija.ednevnik.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

	@Override
	public String getLoggedUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String username = principal.toString();
		return username;
	}

}

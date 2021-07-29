package com.iktakademija.ednevnik.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public String getLoggedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUserUsername = authentication.getName();
		return currentUserUsername;
	}

}

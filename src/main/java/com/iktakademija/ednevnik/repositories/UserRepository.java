package com.iktakademija.ednevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.ednevnik.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
	
	UserEntity findByEmail(String email);
	
	Boolean existsByUsername(String username);
	
	UserEntity findByUsername(String username);
}

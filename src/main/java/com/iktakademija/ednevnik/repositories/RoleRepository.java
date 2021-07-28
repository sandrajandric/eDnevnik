package com.iktakademija.ednevnik.repositories;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.ednevnik.entities.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {

	Optional<RoleEntity> findById(Integer id);
}

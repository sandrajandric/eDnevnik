package com.iktakademija.ednevnik.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iktakademija.ednevnik.entities.TeacherEntity;

public interface TeacherRepository extends CrudRepository<TeacherEntity, Integer> {
	
	@Query("SELECT t FROM TeacherEntity t WHERE t.username = :username")
	TeacherEntity findByUsername(String username);
}

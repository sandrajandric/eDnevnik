package com.iktakademija.ednevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.ednevnik.entities.ClassEntity;
import com.iktakademija.ednevnik.entities.StudentEntity;
import com.iktakademija.ednevnik.entities.enums.EYear;

public interface ClassRepository extends CrudRepository<ClassEntity, Integer> {

	boolean existsByClassNumberAndYear(Integer classNumber, EYear eYear);
	
}

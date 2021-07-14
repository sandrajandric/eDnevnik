package com.iktakademija.ednevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.ednevnik.entities.StudentEntity;

public interface StudentRepository extends CrudRepository<StudentEntity, Integer> {

}

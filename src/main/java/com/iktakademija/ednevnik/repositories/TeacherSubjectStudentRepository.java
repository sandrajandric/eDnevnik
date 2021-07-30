package com.iktakademija.ednevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.ednevnik.entities.Grade;

public interface TeacherSubjectStudentRepository extends CrudRepository<Grade, Integer> {

}

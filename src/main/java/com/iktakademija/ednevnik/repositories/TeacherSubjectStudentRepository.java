package com.iktakademija.ednevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.ednevnik.entities.TeacherSubjectStudentEntity;

public interface TeacherSubjectStudentRepository extends CrudRepository<TeacherSubjectStudentEntity, Integer> {

}

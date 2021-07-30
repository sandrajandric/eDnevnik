package com.iktakademija.ednevnik.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.ednevnik.entities.GradeEntity;
import com.iktakademija.ednevnik.entities.StudentEntity;

public interface GradeRepository extends CrudRepository<GradeEntity, Integer> {

	List<GradeEntity> findByStudent(Integer studentId);
}

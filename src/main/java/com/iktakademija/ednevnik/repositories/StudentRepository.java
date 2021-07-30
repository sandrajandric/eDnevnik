package com.iktakademija.ednevnik.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.iktakademija.ednevnik.entities.StudentEntity;

public interface StudentRepository extends CrudRepository<StudentEntity, Integer> {

	@Query("SELECT COUNT (*) FROM StudentEntity s WHERE s.parent.id = :parentId AND s.id = :studentId")
	Integer existsParentStudent(Integer parentId, Integer studentId);

	@Query("SELECT COUNT (*) FROM StudentEntity s WHERE s.classs.id = :classId AND s.id = :studentId")
	Integer existsClass(Integer classId, Integer studentId);
}

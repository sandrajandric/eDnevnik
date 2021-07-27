package com.iktakademija.ednevnik.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.iktakademija.ednevnik.entities.StudentEntity;

public interface StudentRepository extends CrudRepository<StudentEntity, Integer> {

	@Query("SELECT COUNT (*) FROM StudentEntity s WHERE s.parent.id = :parentId AND s.id = :studentId")
	Integer existsParentStudent(Integer parentId, Integer studentId);

}

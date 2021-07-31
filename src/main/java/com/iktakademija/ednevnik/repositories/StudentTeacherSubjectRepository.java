package com.iktakademija.ednevnik.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.iktakademija.ednevnik.entities.StudentTeacherSubjectEntity;

public interface StudentTeacherSubjectRepository extends CrudRepository<StudentTeacherSubjectEntity, Integer> {

	StudentTeacherSubjectEntity findByStudenttIdAndSubjecttId(Integer studenttId, Integer subjecttId);
	
	List<StudentTeacherSubjectEntity> findByStudenttId(Integer studenttId);
	
	List<StudentTeacherSubjectEntity> findBySubjecttId(Integer subjecttId);
		

}

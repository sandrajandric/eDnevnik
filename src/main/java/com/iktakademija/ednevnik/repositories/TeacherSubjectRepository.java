package com.iktakademija.ednevnik.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.iktakademija.ednevnik.entities.TeacherSubjectEntity;

public interface TeacherSubjectRepository extends CrudRepository<TeacherSubjectEntity, Integer> {

	TeacherSubjectEntity findBySubjectIdAndTeacherId(Integer subjectId, Integer teacherId);
	
	TeacherSubjectEntity findBySubjectId(Integer subject);
	
	List<TeacherSubjectEntity> findByTeacherId(Integer teacher);
	
	@Query("SELECT COUNT (*) FROM TeacherSubjectEntity t WHERE t.subject.id = :subjectId AND t.teacher.id = :teacherId")
	Integer existsSubjectIdAndTeacherId(Integer subjectId, Integer teacherId);

	@Query("DELETE FROM TeacherSubjectEntity t WHERE t.subject.id = :subjectId AND t.teacher.id = :teacherId")
	@Modifying
	@Transactional
	Integer removeSubjectFromTeacher(Integer subjectId, Integer teacherId);
}

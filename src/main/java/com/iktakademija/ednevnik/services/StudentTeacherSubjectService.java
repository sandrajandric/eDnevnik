package com.iktakademija.ednevnik.services;

import com.iktakademija.ednevnik.entities.StudentTeacherSubjectEntity;

public interface StudentTeacherSubjectService {

	Long existsStudentTeacherSubject(Integer studentId, Integer teacherId, Integer subjectId); 
	
	Long existsByStudentAndSubject(Integer studentId, Integer subjectId);
	
	StudentTeacherSubjectEntity findByStudentTeacherSubject(Integer studentId, Integer teacherId, Integer subjectId);
	
	Integer removeSubjectFromStudent(Integer subjectId, Integer studentId);
}

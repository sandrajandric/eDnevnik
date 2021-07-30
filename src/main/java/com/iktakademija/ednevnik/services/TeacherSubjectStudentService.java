package com.iktakademija.ednevnik.services;

import java.util.List;

import com.iktakademija.ednevnik.entities.TeacherSubjectStudentEntity;

public interface TeacherSubjectStudentService {

	Long existsTeacherSubjectStudent(Integer teacherId, Integer subjectId, Integer studentId); 
	
}

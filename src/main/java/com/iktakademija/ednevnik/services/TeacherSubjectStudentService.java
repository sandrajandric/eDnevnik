package com.iktakademija.ednevnik.services;

import java.util.List;

public interface TeacherSubjectStudentService {

	Long existsTeacherSubjectStudent(Integer teacherId, Integer subjectId, Integer studentId); 
	
	List<?> findGradesForSubjectOfTeacher(Integer teacherId, Integer subjectId);
}

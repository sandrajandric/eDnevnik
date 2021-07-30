package com.iktakademija.ednevnik.services;

import java.util.List;

import com.iktakademija.ednevnik.entities.Grade;

public interface TeacherSubjectStudentService {

	Long existsTeacherSubjectStudent(Integer teacherId, Integer subjectId, Integer studentId); 
	
}

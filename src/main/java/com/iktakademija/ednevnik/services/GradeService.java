package com.iktakademija.ednevnik.services;

import java.util.List;

import com.iktakademija.ednevnik.entities.GradeEntity;

public interface GradeService {

	Long existsTeacherSubjectStudent(Integer teacherId, Integer subjectId, Integer studentId); 
	
}

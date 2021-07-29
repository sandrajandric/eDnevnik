package com.iktakademija.ednevnik.services;

public interface TeacherSubjectStudentService {

	Long existsTeacherSubjectStudent(Integer teacherId, Integer subjectId, Integer studentId); 
}

package com.iktakademija.ednevnik.services;

import com.iktakademija.ednevnik.entities.TeacherEntity;

public interface TeacherSubjectService {

	TeacherEntity findTeacherBySubjectId (Integer subjectId);
}

package com.iktakademija.ednevnik.services;

import java.util.List;

import com.iktakademija.ednevnik.entities.GradeEntity;

public interface GradeService {

	List<GradeEntity> findGradesByStudent(Integer studentId);
}

package com.iktakademija.ednevnik.services;

import java.util.List;

public interface GradeService {

	List<?> findAllGradesByStudent(Integer studentId);
}

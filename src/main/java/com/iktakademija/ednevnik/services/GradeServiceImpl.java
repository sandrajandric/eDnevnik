package com.iktakademija.ednevnik.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.iktakademija.ednevnik.entities.GradeEntity;

@Service
public class GradeServiceImpl implements GradeService {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<?> findAllGradesByStudent(Integer studentId) {		
		
		String sql = "SELECT g FROM GradeEntity g INNER JOIN g.teacherSubjectStudent s WHERE s.student.id = :studentId";
		Query query = em.createQuery(sql);

		query.setParameter("studentId", studentId);

		List<GradeEntity> result = new ArrayList<>();
		result = query.getResultList();

		return result;
	}

}

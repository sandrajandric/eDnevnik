package com.iktakademija.ednevnik.services;

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
	public List<GradeEntity> findGradesByStudent(Integer studenttId) {
		String sql = "SELECT g FROM GradeEntity g INNER JOIN g.studentTeacherSubject sts"
				+ " WHERE sts.studentt.id = :studenttId";
		Query query = em.createQuery(sql);

		query.setParameter("studenttId", studenttId);

		List<GradeEntity> result = query.getResultList();

		return result;
	}

}

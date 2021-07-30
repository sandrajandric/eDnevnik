package com.iktakademija.ednevnik.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

@Service
public class GradeServiceImpl implements GradeService {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Long existsTeacherSubjectStudent(Integer teacherId, Integer subjectId, Integer studentId) {
		
		String sql = "SELECT COUNT (*) FROM GradeEntity g INNER JOIN g.teacherSubject s"
				+ " WHERE g.student.id = :studentId AND s.teacher.id = :teacherId AND s.subject.id = :subjectId";
		Query query = em.createQuery(sql);

		query.setParameter("studentId", studentId);
		query.setParameter("teacherId", teacherId);
		query.setParameter("subjectId", subjectId);

		Long result = (Long) query.getSingleResult();

		return result;
	}

	
	
	
	
}

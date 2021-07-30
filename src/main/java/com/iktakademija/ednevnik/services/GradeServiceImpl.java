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

	@Override
	public List<?> findAllGradesByTeacherSubjectStudent(Integer studentId, Integer teacherId, Integer subjectId) {
		String sql = "SELECT g FROM GradeEntity g INNER JOIN g.teacherSubjectStudent s "
				+ "INNER JOIN s.teacherSubject t WHERE s.student.id = :studentId AND t.teacher.id = :teacherId AND t.subject.id = :subjectId";
		Query query = em.createQuery(sql);

		query.setParameter("studentId", studentId);
		query.setParameter("teacherId", teacherId);
		query.setParameter("subjectId", subjectId);

		List<GradeEntity> result = new ArrayList<>();
		result = query.getResultList();

		return result;
	}
	
	

}

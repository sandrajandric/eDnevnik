package com.iktakademija.ednevnik.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import com.iktakademija.ednevnik.entities.TeacherSubjectStudentEntity;

@Service
public class TeacherSubjectStudentServiceImpl implements TeacherSubjectStudentService {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Long existsTeacherSubjectStudent(Integer teacherId, Integer subjectId, Integer studentId) {
		
		String sql = "SELECT COUNT (*) FROM TeacherSubjectStudentEntity t INNER JOIN t.teacherSubject s"
				+ " WHERE t.student.id = :studentId AND s.teacher.id = :teacherId AND s.subject.id = :subjectId";
		Query query = em.createQuery(sql);

		query.setParameter("studentId", studentId);
		query.setParameter("teacherId", teacherId);
		query.setParameter("subjectId", subjectId);

		Long result = (Long) query.getSingleResult();

		return result;
	}

	
	
	
	
}

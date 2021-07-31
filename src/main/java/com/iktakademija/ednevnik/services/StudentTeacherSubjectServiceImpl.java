package com.iktakademija.ednevnik.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iktakademija.ednevnik.entities.StudentTeacherSubjectEntity;

@Service
public class StudentTeacherSubjectServiceImpl implements StudentTeacherSubjectService {

	@PersistenceContext
	private EntityManager em;

	@Override
	public Long existsStudentTeacherSubject(Integer studenttId, Integer teacherId, Integer subjectId) {
		String sql = "SELECT COUNT (*) FROM StudentTeacherSubject s INNER JOIN s.subjectt ts"
				+ " WHERE s.studentt.id = :studenttId AND ts.teacher.id = :teacherId AND ts.subject.id = :subjectId";
		Query query = em.createQuery(sql);

		query.setParameter("studenttId", studenttId);
		query.setParameter("teacherId", teacherId);
		query.setParameter("subjectId", subjectId);

		Long result = (Long) query.getSingleResult();

		return result;
	}

	@Override
	public Long existsByStudentAndSubject(Integer studenttId, Integer subjectId) {
		String sql = "SELECT COUNT (*) FROM StudentTeacherSubjectEntity s INNER JOIN s.subjectt ts"
				+ " WHERE s.studentt.id = :studenttId AND ts.subject.id = :subjectId";
		Query query = em.createQuery(sql);

		query.setParameter("studenttId", studenttId);
		query.setParameter("subjectId", subjectId);

		Long result = (Long) query.getSingleResult();

		return result;
	}

	@Override
	public StudentTeacherSubjectEntity findByStudentTeacherSubject(Integer studenttId, Integer teacherId,
			Integer subjectId) {
		String sql = "SELECT s FROM StudentTeacherSubjectEntity s INNER JOIN s.subjectt ts"
				+ " WHERE s.studentt.id = :studenttId AND ts.subject.id = :subjectId AND ts.teacher.id = :teacherId";
		Query query = em.createQuery(sql);

		query.setParameter("studenttId", studenttId);
		query.setParameter("subjectId", subjectId);
		query.setParameter("teacherId", teacherId);

		StudentTeacherSubjectEntity result = (StudentTeacherSubjectEntity) query.getSingleResult();

		return result;
	}

	@Override
	@Transactional
	public Integer removeSubjectFromStudent(Integer subjectId, Integer studentId) {
		String sql = "DELETE FROM StudentTeacherSubjectEntity sts WHERE sts.subjectt.id IN (SELECT ts.id FROM TeacherSubjectEntity ts"
				+ " WHERE ts.subject.id = :subjectId AND sts.studentt.id = :studentId)";
		Query query = em.createQuery(sql);

		query.setParameter("studentId", studentId);
		query.setParameter("subjectId", subjectId);

		Integer result = query.executeUpdate();

		return result;
	}

	
	
	
	
}

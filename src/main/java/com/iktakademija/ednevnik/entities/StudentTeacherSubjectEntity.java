package com.iktakademija.ednevnik.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "student_teacher_subject")
public class StudentTeacherSubjectEntity {

	@Id
	@GeneratedValue
	private Integer id;
	
	@JsonManagedReference(value = "stsu")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "studentt")
	private StudentEntity studentt;
	
	@JsonBackReference(value = "sus")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "subjectt")
	private TeacherSubjectEntity subjectt;
	
	@JsonBackReference(value = "tspp")
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "studentTeacherSubject")
	private List<GradeEntity> grades = new ArrayList<>();
	
	@Version
	private Integer version;

	public StudentTeacherSubjectEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StudentTeacherSubjectEntity(Integer id, StudentEntity studentt, TeacherSubjectEntity subjectt,
			List<GradeEntity> listens) {
		super();
		this.id = id;
		this.studentt = studentt;
		this.subjectt = subjectt;
		this.grades = listens;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public StudentEntity getStudentt() {
		return studentt;
	}

	public void setStudentt(StudentEntity studentt) {
		this.studentt = studentt;
	}

	public TeacherSubjectEntity getSubjectt() {
		return subjectt;
	}

	public void setSubjectt(TeacherSubjectEntity subjectt) {
		this.subjectt = subjectt;
	}

	public List<GradeEntity> getGrades() {
		return grades;
	}

	public void setGrades(List<GradeEntity> grades) {
		this.grades = grades;
	}

	@Override
	public String toString() {
		return "Student: " + getStudentt().getName() + " " + getStudentt().getSurname() + 
				"Teacher: " + getSubjectt().getTeacher().getName() + " " + getSubjectt().getTeacher().getSurname();
	}
	
}

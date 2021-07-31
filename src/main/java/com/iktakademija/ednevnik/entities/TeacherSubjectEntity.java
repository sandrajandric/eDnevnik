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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.ednevnik.security.Views;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "teacher_subject")
public class TeacherSubjectEntity {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@JsonManagedReference(value = "tst")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher")
	private TeacherEntity teacher;
	
	@JsonBackReference(value = "tss")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "subject")
	private SubjectEntity subject;
	
	
	@JsonManagedReference(value = "sus")
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "subjectt")
	private List<StudentTeacherSubjectEntity> subjects = new ArrayList<>();
	
	@Version
	private Integer version;

	public TeacherSubjectEntity() {
		super();
		// TODO Auto-generated constructor stub
	}


	public TeacherSubjectEntity(Integer id, TeacherEntity teacher, SubjectEntity subject,
			List<StudentTeacherSubjectEntity> subjects) {
		super();
		this.id = id;
		this.teacher = teacher;
		this.subject = subject;
		this.subjects = subjects;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public TeacherEntity getTeacher() {
		return teacher;
	}


	public void setTeacher(TeacherEntity teacher) {
		this.teacher = teacher;
	}


	public SubjectEntity getSubject() {
		return subject;
	}


	public void setSubject(SubjectEntity subject) {
		this.subject = subject;
	}


	public List<StudentTeacherSubjectEntity> getSubjects() {
		return subjects;
	}


	public void setSubjects(List<StudentTeacherSubjectEntity> subjects) {
		this.subjects = subjects;
	}

	@Override
	public String toString() {
		return "Teacher: " + getTeacher().getName() + " " + getTeacher().getSurname() +
				"Subject: " + getSubject().getNameOfSubject();
	}
	
	
}

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
	
	@JsonView(Views.Admin.class)
	@Id
	@GeneratedValue
	private Integer id;
	
	@JsonView(Views.Private.class)
	@JsonBackReference(value = "tst")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher")
	private TeacherEntity teacher;
	
	@JsonView(Views.Private.class)
	@JsonBackReference(value = "tss")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "subject")
	private SubjectEntity subject;
	
	@JsonManagedReference(value = "tse")
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<TeacherSubjectStudentEntity> teacherSubjectStudent = new ArrayList<>();

	public TeacherSubjectEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TeacherSubjectEntity(Integer id, TeacherEntity teacher, SubjectEntity subject,
			List<TeacherSubjectStudentEntity> teacherSubjectStudent) {
		super();
		this.id = id;
		this.teacher = teacher;
		this.subject = subject;
		this.teacherSubjectStudent = teacherSubjectStudent;
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

	public List<TeacherSubjectStudentEntity> getTeacherSubjectStudent() {
		return teacherSubjectStudent;
	}

	public void setTeacherSubjectStudent(List<TeacherSubjectStudentEntity> teacherSubjectStudent) {
		this.teacherSubjectStudent = teacherSubjectStudent;
	}

	
}

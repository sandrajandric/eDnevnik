package com.iktakademija.ednevnik.entities;

import java.time.LocalDate;
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
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.ednevnik.entities.enums.EGradeType;
import com.iktakademija.ednevnik.security.Views;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "grade")
public class GradeEntity {

	@JsonView(Views.Admin.class)
	@Id
	@GeneratedValue
	private Integer id;

	@JsonView(Views.Private.class)
	@NotNull(message = "Grade type must not be null. Accepted values are: TEST, ORAL, CLASS_ACTIVITY")
	private EGradeType gradeType;
	
	@JsonView(Views.Private.class)
	@Range(min = 1, max = 5)
	@NotNull(message = "Grade must not be left blank. Accepted values are between {min} and {max}.")
	private Integer grade;
	
	@JsonView(Views.Private.class)
	@JsonFormat(pattern = "dd-MM-yyyy")
	private LocalDate date;
	
	@JsonView(Views.Private.class)
	@JsonManagedReference(value = "tspp")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "student")
	private StudentEntity student;

	@JsonBackReference(value = "tse")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "teacherSubject")
	private TeacherSubjectEntity teacherSubject;
	
	@Version
	private Integer version;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}



	public EGradeType getGradeType() {
		return gradeType;
	}



	public void setGradeType(EGradeType gradeType) {
		this.gradeType = gradeType;
	}



	public Integer getGrade() {
		return grade;
	}



	public void setGrade(Integer grade) {
		this.grade = grade;
	}



	public LocalDate getDate() {
		return date;
	}



	public void setDate(LocalDate date) {
		this.date = date;
	}



	public StudentEntity getStudent() {
		return student;
	}



	public void setStudent(StudentEntity student) {
		this.student = student;
	}



	public TeacherSubjectEntity getTeacherSubject() {
		return teacherSubject;
	}



	public void setTeacherSubject(TeacherSubjectEntity teacherSubject) {
		this.teacherSubject = teacherSubject;
	}


	public GradeEntity(Integer id,
			@NotNull(message = "Grade type must not be null. Accepted values are: TEST, ORAL, CLASS_ACTIVITY") EGradeType gradeType,
			@Range(min = 1, max = 5) @NotNull(message = "Grade must not be left blank. Accepted values are between {min} and {max}.") Integer grade,
			LocalDate date, StudentEntity student, TeacherSubjectEntity teacherSubject) {
		super();
		this.id = id;
		this.gradeType = gradeType;
		this.grade = grade;
		this.date = date;
		this.student = student;
		this.teacherSubject = teacherSubject;
	}



	public GradeEntity() {
		super();
		// TODO Auto-generated constructor stub
	}



	@Override
	public String toString() {
		return "GradeType: " + getGradeType() + "\nGrade: " + getGrade() + "\nDate: " + getDate() +
				"\nStudent: " + getStudent() + "\nTeacher&Subject: " + getTeacherSubject();
	}

}

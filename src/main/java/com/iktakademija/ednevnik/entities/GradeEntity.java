package com.iktakademija.ednevnik.entities;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.ednevnik.entities.enums.EGradeType;
import com.iktakademija.ednevnik.security.Views;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
	
	@JsonManagedReference(value = "gtsp")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "hasGrades")
	private Grade hasGrades;
		
	@JsonView(Views.Admin.class)
	@Version
	private Integer version;

	public GradeEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GradeEntity(Integer id,
			@NotNull(message = "Grade type must not be null. Accepted values are: TEST, ORAL, CLASS_ACTIVITY") EGradeType gradeType,
			@Range(min = 1, max = 5) @NotNull(message = "Grade must not be left blank. Accepted values are between {min} and {max}.") Integer grade,
			LocalDate date, Grade hasGrades) {
		super();
		this.id = id;
		this.gradeType = gradeType;
		this.grade = grade;
		this.date = date;
		this.hasGrades = hasGrades;
	}

	


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

	public Grade getHasGrades() {
		return hasGrades;
	}

	public void setHasGrades(Grade hasGrades) {
		this.hasGrades = hasGrades;
	}

	@Override
	public String toString() {
		return "GradeType: " + getGradeType() + "\nGrade: " + getGrade() + "\nDate: " + getDate() +
			"\nStudent: " + getHasGrades().getStudent().getName() + " " +
			getHasGrades().getStudent().getSurname() + "\nTeacher: " + getHasGrades().getTeacherSubject().getTeacher().getName()
			+ " " + getHasGrades().getTeacherSubject().getTeacher().getSurname() + "\nSubject: "
			+ getHasGrades().getTeacherSubject().getSubject().getNameOfSubject();
	}
	
}

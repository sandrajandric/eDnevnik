package com.iktakademija.ednevnik.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.ednevnik.entities.enums.EYear;
import com.iktakademija.ednevnik.security.Views;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "class")
public class ClassEntity {

	@Id
	@GeneratedValue
	@Column(name = "class_id")
	private Integer id;
	
	@Column(nullable = false)
	@NotNull(message = "Class number must not be null. Class number must be between {min} and {max}")
	@Min(value = 1)
	@Max(value = 9)
	private Integer classNumber;
	
	@Enumerated(EnumType.STRING)
	private EYear year;
	
	@JsonManagedReference(value = "pc")
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "classs")
	private List<StudentEntity> students = new ArrayList<>();
	
	@JsonManagedReference(value = "tc")
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "inChargeOfClass")
	private TeacherEntity homeroomTeacher;
	
	@Version
	private Integer version;

	public ClassEntity() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getClassNumber() {
		return classNumber;
	}

	public void setClassNumber(Integer classNumber) {
		this.classNumber = classNumber;
	}

	public EYear getYear() {
		return year;
	}

	public void setYear(EYear year) {
		this.year = year;
	}

	public List<StudentEntity> getStudents() {
		return students;
	}

	public void setStudents(List<StudentEntity> students) {
		this.students = students;
	}

	public TeacherEntity getHomeroomTeacher() {
		return homeroomTeacher;
	}

	public void setHomeroomTeacher(TeacherEntity homeroomTeacher) {
		this.homeroomTeacher = homeroomTeacher;
	}
	
	@Override
	public String toString() {
		return "Year: " + getYear() + ", ClassNumber: " + getClassNumber() + ", HomeroomTeacher: " + getHomeroomTeacher().getName()
				+ " " + getHomeroomTeacher().getSurname();
	}
}

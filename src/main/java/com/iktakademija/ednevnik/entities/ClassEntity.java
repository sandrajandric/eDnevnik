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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.ednevnik.entities.enums.EYear;
import com.iktakademija.ednevnik.security.Views;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "class")
public class ClassEntity {

	@JsonView(Views.Admin.class)
	@Id
	@GeneratedValue
	@Column(name = "class_id")
	private Integer id;
	
	@JsonView(Views.Private.class)
	@Column(nullable = false)
	@NotNull(message = "Class number must not be null. Class number must be between {min} and {max}")
	@Min(value = 1)
	@Max(value = 9)
	private Integer classNumber;
	
	@JsonView(Views.Private.class)
	@Enumerated(EnumType.STRING)
	private EYear year;
	
	@JsonView(Views.Private.class)
	@JsonManagedReference(value = "pc")
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "classs")
	private List<StudentEntity> pupils = new ArrayList<>();
	
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "homeroomTeacher")
	private TeacherEntity homeroomTeacher;
	
	@JsonView(Views.Admin.class)
	@Version
	private Integer version;

	public ClassEntity() {
		super();
		// TODO Auto-generated constructor stub
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

	public List<StudentEntity> getPupils() {
		return pupils;
	}

	public void setPupils(List<StudentEntity> pupils) {
		this.pupils = pupils;
	}

	public TeacherEntity getHomeroomTeacher() {
		return homeroomTeacher;
	}

	public void setHomeroomTeacher(TeacherEntity homeroomTeacher) {
		this.homeroomTeacher = homeroomTeacher;
	}
	
	
}

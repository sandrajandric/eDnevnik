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
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.ednevnik.entities.enums.ESemester;
import com.iktakademija.ednevnik.entities.enums.EYear;
import com.iktakademija.ednevnik.security.Views;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "subject")
public class SubjectEntity {

	@JsonView(Views.Admin.class)
	@Id
	@GeneratedValue
	@Column(name = "subject_id")
	private Integer id;
	
	@JsonView(Views.Private.class)
	@Column(nullable = false)
	@NotBlank(message = "Subject name must not be left blank.")
	@Size(min = 2, max = 30, message = "Subject name must be between {min} and {max} characters long.")
	private String nameOfSubject;
	
	@JsonView(Views.Private.class)
	@NotNull(message = "Weekly hours must not be left blank. Weekly hours must be between {min} and {max}.")
	@Min(value = 0)
	@Max(value = 40)
	private Integer weeklyHours;
	
	@JsonView(Views.Private.class)
	@Enumerated(EnumType.STRING)
	private ESemester semester;
	
	@JsonView(Views.Private.class)
	@Enumerated(EnumType.STRING)
	private EYear subjectForYear;
	
	@JsonManagedReference(value = "tss")
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "subject")
	private List<TeacherSubjectEntity> hasTeachers = new ArrayList<>();
	
	
	
	@JsonView(Views.Admin.class)
	@Version
	private Integer version;
	
	
	public SubjectEntity() {
		super();
		// TODO Auto-generated constructor stub
	}


	public SubjectEntity(Integer id,
			@NotBlank(message = "Subject name must not be left blank.") @Size(min = 2, max = 30, message = "Subject name must be between {min} and {max} characters long.") String nameOfSubject,
			@NotNull(message = "Weekly hours must not be left blank. Weekly hours must be between {min} and {max}.") @Min(0) @Max(40) Integer weeklyHours,
			ESemester semester, EYear subjectForYear, List<TeacherSubjectEntity> hasTeachers) {
		super();
		this.id = id;
		this.nameOfSubject = nameOfSubject;
		this.weeklyHours = weeklyHours;
		this.semester = semester;
		this.subjectForYear = subjectForYear;
		this.hasTeachers = hasTeachers;
	}

	



	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getNameOfSubject() {
		return nameOfSubject;
	}


	public void setNameOfSubject(String nameOfSubject) {
		this.nameOfSubject = nameOfSubject;
	}


	public Integer getWeeklyHours() {
		return weeklyHours;
	}


	public void setWeeklyHours(Integer weeklyHours) {
		this.weeklyHours = weeklyHours;
	}


	public ESemester getSemester() {
		return semester;
	}


	public void setSemester(ESemester semester) {
		this.semester = semester;
	}


	public EYear getSubjectForYear() {
		return subjectForYear;
	}


	public void setSubjectForYear(EYear subjectForYear) {
		this.subjectForYear = subjectForYear;
	}


	public List<TeacherSubjectEntity> getHasTeachers() {
		return hasTeachers;
	}


	public void setHasTeachers(List<TeacherSubjectEntity> hasTeachers) {
		this.hasTeachers = hasTeachers;
	}


	@Override
	public String toString() {
		return "NameOfSubject: " + getNameOfSubject() + ", Year: " + getSubjectForYear();
	}
}

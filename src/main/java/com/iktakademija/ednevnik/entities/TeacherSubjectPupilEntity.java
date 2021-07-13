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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.ednevnik.security.Views;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "teacher_subject_pupil")
public class TeacherSubjectPupilEntity {

	@JsonView(Views.Admin.class)
	@Id
	@GeneratedValue
	private Integer id;
	
	@JsonView(Views.Private.class)
	@JsonBackReference(value = "tspp")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "pupil")
	private PupilEntity pupil;
	
	@JsonView(Views.Private.class)
	@JsonManagedReference(value = "gtsp")
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher_subject_pupil")
	private List<GradeEntity> hasGrades = new ArrayList<>();

	public TeacherSubjectPupilEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public PupilEntity getPupil() {
		return pupil;
	}

	public void setPupil(PupilEntity pupil) {
		this.pupil = pupil;
	}

	public List<GradeEntity> getHasGrades() {
		return hasGrades;
	}

	public void setHasGrades(List<GradeEntity> hasGrades) {
		this.hasGrades = hasGrades;
	}
	
	
	
}

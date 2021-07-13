package com.iktakademija.ednevnik.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "pupil")
public class PupilEntity extends UserEntity {

	@JsonView(Views.Private.class)
	@JsonBackReference(value = "pp")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent")
	private ParentEntity parent;
	
	@JsonView(Views.Private.class)
	@JsonBackReference(value = "pc")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "classs")
	private ClassEntity classs;
	
	@JsonView(Views.Private.class)
	@JsonManagedReference(value = "tspp")
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "pupil")
	private List<TeacherSubjectPupilEntity> listens = new ArrayList<>();


	public PupilEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ParentEntity getParent() {
		return parent;
	}

	public void setParent(ParentEntity parent) {
		this.parent = parent;
	}

	public ClassEntity getClasss() {
		return classs;
	}

	public void setClasss(ClassEntity classs) {
		this.classs = classs;
	}

	public List<TeacherSubjectPupilEntity> getListens() {
		return listens;
	}

	public void setListens(List<TeacherSubjectPupilEntity> listens) {
		this.listens = listens;
	}
	
	
	
}

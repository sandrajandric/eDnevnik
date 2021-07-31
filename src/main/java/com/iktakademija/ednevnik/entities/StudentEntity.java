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
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.ednevnik.security.Views;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "student")
public class StudentEntity extends UserEntity {

	@JsonBackReference(value = "pp")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent")
	private ParentEntity parent;
	
	@JsonBackReference(value = "pc")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "classs")
	private ClassEntity classs;
	
	@JsonBackReference(value = "stsu")
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "studentt")
	private List<StudentTeacherSubjectEntity> students = new ArrayList<>();
	

	public StudentEntity() {
		super();
		// TODO Auto-generated constructor stub
	}


	public StudentEntity(ParentEntity parent, ClassEntity classs, List<StudentTeacherSubjectEntity> students) {
		super();
		this.parent = parent;
		this.classs = classs;
		this.students = students;
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


	public List<StudentTeacherSubjectEntity> getStudents() {
		return students;
	}


	public void setStudents(List<StudentTeacherSubjectEntity> students) {
		this.students = students;
	}

	@Override
	public String toString() {
		return super.toString() + ", Parent: " + getParent().getName() + " " +
				getParent().getSurname() + ", Class: " + getClasss().getYear() + " " + getClasss().getClassNumber();
	}
	
	
}

package com.iktakademija.ednevnik.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.ednevnik.security.Views;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "teacher")
public class TeacherEntity extends UserEntity {

	@JsonView(Views.Private.class)
	@JsonManagedReference(value = "tst")
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "teacher")
	private List<TeacherSubjectEntity> teaches = new ArrayList<>();
	
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "homeroomTeacher")
	private ClassEntity inChargeOfClass;

	public TeacherEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<TeacherSubjectEntity> getTeaches() {
		return teaches;
	}

	public void setTeaches(List<TeacherSubjectEntity> teaches) {
		this.teaches = teaches;
	}

	public ClassEntity getInChargeOfClass() {
		return inChargeOfClass;
	}

	public void setInChargeOfClass(ClassEntity inChargeOfClass) {
		this.inChargeOfClass = inChargeOfClass;
	}

	
}

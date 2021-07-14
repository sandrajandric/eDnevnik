package com.iktakademija.ednevnik.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.ednevnik.security.Views;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "parent")
public class ParentEntity extends UserEntity {

	@JsonView(Views.Private.class)
	@JsonManagedReference(value = "pp")
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "parent")
	private List<StudentEntity> pupils = new ArrayList<>();

	public ParentEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<StudentEntity> getPupils() {
		return pupils;
	}

	public void setPupils(List<StudentEntity> pupils) {
		this.pupils = pupils;
	}
	
	
}

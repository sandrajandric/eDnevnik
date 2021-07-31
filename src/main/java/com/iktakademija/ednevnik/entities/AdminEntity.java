package com.iktakademija.ednevnik.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "admin")
public class AdminEntity extends UserEntity {

	@Override
	public String toString() {
		return super.toString();
	}
}

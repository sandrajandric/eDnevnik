package com.iktakademija.ednevnik.entities.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.iktakademija.ednevnik.entities.enums.EYear;

public class ClassDTOPut {

	private Integer classNumber;
	
	@Enumerated(EnumType.STRING)
	private EYear year;

	public ClassDTOPut() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ClassDTOPut(Integer classNumber, EYear year) {
		super();
		this.classNumber = classNumber;
		this.year = year;
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
	
	
}

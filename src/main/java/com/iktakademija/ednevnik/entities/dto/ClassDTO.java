package com.iktakademija.ednevnik.entities.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.iktakademija.ednevnik.entities.enums.EYear;

public class ClassDTO {

	@NotNull(message = "Class number must not be null. Class number must be between {min} and {max}")
	private Integer classNumber;
	
	@NotBlank(message = "Year must be one of the values: I, II, III, IV, V, VI, VII, VIII, IX, X")
	private EYear year;

	public ClassDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ClassDTO(
			@NotNull(message = "Class number must not be null. Class number must be between {min} and {max}") Integer classNumber,
			@NotBlank(message = "Year must be one of the values: I, II, III, IV, V, VI, VII, VIII, IX, X") EYear year) {
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

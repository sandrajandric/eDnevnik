package com.iktakademija.ednevnik.entities.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.iktakademija.ednevnik.entities.enums.EGradeType;

public class GradeDTO {

	@NotNull(message = "Grade type must not be null. Accepted values are: TEST, ORAL, CLASS_ACTIVITY")
	private EGradeType gradeType;
	
	@Range(min = 1, max = 5)
	@NotNull(message = "Grade must not be left blank. Accepted values are between {min} and {max}.")
	private Integer grade;
	
	public GradeDTO() {
		super();
	}

	public GradeDTO(
			@NotBlank(message = "Grade type must not be null. Accepted values are: TEST, ORAL, CLASS_ACTIVITY") EGradeType gradeType,
			@Range(min = 1, max = 5) @NotBlank(message = "Grade must not be left blank. Accepted values are between {min} and {max}.") Integer grade) {
		super();
		this.gradeType = gradeType;
		this.grade = grade;
	}

	public EGradeType getGradeType() {
		return gradeType;
	}

	public void setGradeType(EGradeType gradeType) {
		this.gradeType = gradeType;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
		
	}

	
}

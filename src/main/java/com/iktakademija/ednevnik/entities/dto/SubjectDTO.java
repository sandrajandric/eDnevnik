package com.iktakademija.ednevnik.entities.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.iktakademija.ednevnik.entities.enums.ESemester;
import com.iktakademija.ednevnik.entities.enums.EYear;

public class SubjectDTO {

	@NotBlank(message = "Subject name must not be left blank.")
	@Size(min = 2, max = 30, message = "Subject name must be between {min} and {max} characters long.")
	private String nameOfSubject;
	
	@NotNull(message = "Weekly hours must not be left blank. Weekly hours must be between {min} and {max}.")
	@Min(value = 0)
	@Max(value = 40)
	private Integer weeklyHours;
	
	@NotBlank(message = "Semester must be one of the values: FIRST, SECOND")
	private ESemester semester;
	
	@NotBlank(message = "Year must be one of the values: I, II, III, IV, V, VI, VII, VIII, IX, X")
	private EYear subjectForYear;

	public SubjectDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SubjectDTO(
			@NotBlank(message = "Subject name must not be left blank.") @Size(min = 2, max = 30, message = "Subject name must be between {min} and {max} characters long.") String nameOfSubject,
			@NotNull(message = "Weekly hours must not be left blank. Weekly hours must be between {min} and {max}.") @Min(0) @Max(40) Integer weeklyHours,
			@NotBlank(message = "Semester must be one of the values: FIRST, SECOND") ESemester semester,
			@NotBlank(message = "Year must be one of the values: I, II, III, IV, V, VI, VII, VIII, IX, X") EYear subjectForYear) {
		super();
		this.nameOfSubject = nameOfSubject;
		this.weeklyHours = weeklyHours;
		this.semester = semester;
		this.subjectForYear = subjectForYear;
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
	
	
}

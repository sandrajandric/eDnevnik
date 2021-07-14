package com.iktakademija.ednevnik.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktakademija.ednevnik.controllers.util.RESTError;
import com.iktakademija.ednevnik.entities.SubjectEntity;
import com.iktakademija.ednevnik.repositories.SubjectRepository;

@RestController
@RequestMapping(value = "/api/v1/subjects")
public class SubjectController {

	@Autowired
	private SubjectRepository subjectRepository;
	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage)
				.collect(Collectors.joining(" "));
	}
	
	// lista svih predmeta
	@RequestMapping(method = RequestMethod.GET, value = "")
	public ResponseEntity<?> getAllSubjects() {
		List<SubjectEntity> subjects = new ArrayList<>();
		subjects = (List<SubjectEntity>) subjectRepository.findAll();
		if (!subjects.isEmpty()) {
			return new ResponseEntity<List<SubjectEntity>>(subjects, HttpStatus.OK);
		} else {
			return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Subjects not found"), HttpStatus.NOT_FOUND);
		}
	}
	
	// pronadji predmet po id
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getSubjectById(@PathVariable Integer id) {
		if (subjectRepository.existsById(id)) {
			SubjectEntity subjectEntity = subjectRepository.findById(id).get();
			return new ResponseEntity<SubjectEntity>(subjectEntity, HttpStatus.OK);
		} else {
			return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Subject with id number " + id + " not found"), HttpStatus.NOT_FOUND);
		}
	}
	
	// dodaj novi predmet
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createSubject(@RequestBody SubjectEntity subjectEntity,
			 BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		SubjectEntity newSubject = new SubjectEntity();
		newSubject.setNameOfSubject(subjectEntity.getNameOfSubject());
		newSubject.setWeeklyHours(subjectEntity.getWeeklyHours());
		newSubject.setSemester(subjectEntity.getSemester());
		newSubject.setSubjectForYear(subjectEntity.getSubjectForYear());

		subjectRepository.save(newSubject);
		return new ResponseEntity<SubjectEntity>(newSubject, HttpStatus.CREATED);
	}
	
	// izmeni predmet
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateSubject(@PathVariable Integer id, 
			@RequestBody SubjectEntity updatedSubject, BindingResult result) {
		
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (subjectRepository.existsById(id)) {
			SubjectEntity subjectEntity = subjectRepository.findById(id).get();

			if (updatedSubject.getNameOfSubject() != null) {
				subjectEntity.setNameOfSubject(updatedSubject.getNameOfSubject());
			}
			if (updatedSubject.getWeeklyHours() != null) {
				subjectEntity.setWeeklyHours(updatedSubject.getWeeklyHours());
			}
			if (updatedSubject.getSemester() != null) {
				subjectEntity.setSemester(updatedSubject.getSemester());
			}
			if (updatedSubject.getSubjectForYear() != null) {
				subjectEntity.setSubjectForYear(updatedSubject.getSubjectForYear());
			}
			
			subjectRepository.save(updatedSubject);
			return new ResponseEntity<SubjectEntity>(updatedSubject, HttpStatus.OK);
		}
		return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Subject with id number " + id + " not found"), HttpStatus.NOT_FOUND);
	}
	
	// obrisi predmet
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteSubject(@PathVariable Integer id) {
		if (subjectRepository.existsById(id)) {
			subjectRepository.deleteById(id);
			return new ResponseEntity<SubjectEntity>(HttpStatus.OK);
		} else {
			return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Subject with id number " + id + " not found"), HttpStatus.NOT_FOUND);
		}
	}
}

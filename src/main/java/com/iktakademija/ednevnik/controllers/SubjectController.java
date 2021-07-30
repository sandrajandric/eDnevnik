package com.iktakademija.ednevnik.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktakademija.ednevnik.controllers.util.RESTError;
import com.iktakademija.ednevnik.entities.SubjectEntity;
import com.iktakademija.ednevnik.entities.dto.SubjectDTO;
import com.iktakademija.ednevnik.entities.dto.SubjectDTOPut;
import com.iktakademija.ednevnik.repositories.SubjectRepository;

@RestController
@RequestMapping(value = "/api/v1/subjects")
@Secured("ROLE_ADMIN")
public class SubjectController {
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

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
			logger.info("Viewed all subjects.");
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
			logger.info("Viewed subject with id number " + id);
			return new ResponseEntity<SubjectEntity>(subjectEntity, HttpStatus.OK);
		} else {
			return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Subject with id number " + id + " not found"), HttpStatus.NOT_FOUND);
		}
	}
	
	// dodaj novi predmet
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createSubject(@RequestBody SubjectDTO subjectDTO,
			 BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		SubjectEntity newSubject = new SubjectEntity();
		newSubject.setNameOfSubject(subjectDTO.getNameOfSubject());
		newSubject.setWeeklyHours(subjectDTO.getWeeklyHours());
		newSubject.setSemester(subjectDTO.getSemester());
		newSubject.setSubjectForYear(subjectDTO.getSubjectForYear());

		subjectRepository.save(newSubject);
		logger.info("Created subject " + newSubject.toString());
		return new ResponseEntity<SubjectEntity>(newSubject, HttpStatus.CREATED);
	}
	
	// izmeni predmet
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateSubject(@PathVariable Integer id, 
			@RequestBody SubjectDTOPut subjectDTO, BindingResult result) {
		
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (subjectRepository.existsById(id)) {
			
			SubjectEntity subjectEntity = subjectRepository.findById(id).get();
			
			if (!(subjectDTO.getNameOfSubject().equalsIgnoreCase(subjectEntity.getNameOfSubject()))
					&& (subjectDTO.getSubjectForYear().equals(subjectEntity.getSubjectForYear()))) {
				if (subjectDTO.getNameOfSubject() != null) {
					subjectEntity.setNameOfSubject(subjectDTO.getNameOfSubject());
				}
				if (subjectDTO.getWeeklyHours() != null) {
					subjectEntity.setWeeklyHours(subjectDTO.getWeeklyHours());
				}
				if (subjectDTO.getSemester() != null) {
					subjectEntity.setSemester(subjectDTO.getSemester());
				}
				if (subjectDTO.getSubjectForYear() != null) {
					subjectEntity.setSubjectForYear(subjectDTO.getSubjectForYear());
				}
				
				subjectRepository.save(subjectEntity);
				logger.info("Updated subject with id number " + id);
				return new ResponseEntity<SubjectEntity>(subjectEntity, HttpStatus.OK);
			} else {
				return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.BAD_REQUEST.value(), "Subject with name "
					+ subjectEntity.getNameOfSubject() + " for year " + subjectEntity.getSubjectForYear() + " already exists."), HttpStatus.BAD_REQUEST);
			}
		}
		return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Subject with id number " + id + " not found"), HttpStatus.NOT_FOUND);
	}
	
	// obrisi predmet
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteSubject(@PathVariable Integer id) {
		if (subjectRepository.existsById(id)) {
			subjectRepository.deleteById(id);
			logger.info("Deleted subject with id number " + id);
			return new ResponseEntity<SubjectEntity>(HttpStatus.OK);
		} else {
			return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Subject with id number " + id + " not found"), HttpStatus.NOT_FOUND);
		}
	}

}

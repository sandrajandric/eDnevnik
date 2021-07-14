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
import com.iktakademija.ednevnik.entities.TeacherEntity;
import com.iktakademija.ednevnik.repositories.TeacherRepository;

@RestController
@RequestMapping(value = "/api/v1/teachers")
public class TeacherController {

	@Autowired
	private TeacherRepository teacherRepository;
	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage)
				.collect(Collectors.joining(" "));
	}
	
	// lista svih nastavnika
	@RequestMapping(method = RequestMethod.GET, value = "")
	public ResponseEntity<?> getAllTeachers() {
		List<TeacherEntity> teachers = new ArrayList<>();
		teachers = (List<TeacherEntity>) teacherRepository.findAll();
		if (!teachers.isEmpty()) {
			return new ResponseEntity<List<TeacherEntity>>(teachers, HttpStatus.OK);
		} else {
			return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Teachers not found"), HttpStatus.NOT_FOUND);
		}
	}
	
	// pronadji nastavnika po id
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getTeacherById(@PathVariable Integer id) {
		if (teacherRepository.existsById(id)) {
			TeacherEntity teacherEntity = teacherRepository.findById(id).get();
			return new ResponseEntity<TeacherEntity>(teacherEntity, HttpStatus.OK);
		} else {
			return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Teacher with id number " + id + " not found"), HttpStatus.NOT_FOUND);
		}
	}
	
	// dodaj novog nastavnika
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createTeacher(@RequestBody TeacherEntity teacherEntity,
			 BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		TeacherEntity newTeacher = new TeacherEntity();
		newTeacher.setName(teacherEntity.getName());
		newTeacher.setSurname(teacherEntity.getSurname());
		newTeacher.setEmail(teacherEntity.getEmail());
		newTeacher.setPassword(teacherEntity.getPassword());
		newTeacher.setUsername(teacherEntity.getUsername());

		teacherRepository.save(newTeacher);
		return new ResponseEntity<TeacherEntity>(newTeacher, HttpStatus.CREATED);
	}
	
	// izmeni nastavnika
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateTeacher(@PathVariable Integer id, 
			@RequestBody TeacherEntity updatedTeacher, BindingResult result) {
		
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (teacherRepository.existsById(id)) {
			TeacherEntity teacherEntity = teacherRepository.findById(id).get();

			if (updatedTeacher.getName() != null) {
				teacherEntity.setName(updatedTeacher.getName());
			}
			if (updatedTeacher.getSurname() != null) {
				teacherEntity.setSurname(updatedTeacher.getSurname());
			}
			if (updatedTeacher.getEmail() != null) {
				teacherEntity.setEmail(updatedTeacher.getEmail());
			}
			if (updatedTeacher.getPassword() != null) {
				teacherEntity.setPassword(updatedTeacher.getPassword());
			}
			if (updatedTeacher.getUsername() != null) {
				teacherEntity.setUsername(updatedTeacher.getUsername());
			}
		
			
			teacherRepository.save(updatedTeacher);
			return new ResponseEntity<TeacherEntity>(updatedTeacher, HttpStatus.OK);
		}
		else {
			return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Teacher with id number " + id + " not found"), HttpStatus.NOT_FOUND);
		}
	}
	
	// obrisi nastavnika
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteTeacher(@PathVariable Integer id) {
		if (teacherRepository.existsById(id)) {
			teacherRepository.deleteById(id);
			return new ResponseEntity<TeacherEntity>(HttpStatus.OK);
		} else {
			return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Teacher with id number " + id + " not found"), HttpStatus.NOT_FOUND);
		}
	}
}

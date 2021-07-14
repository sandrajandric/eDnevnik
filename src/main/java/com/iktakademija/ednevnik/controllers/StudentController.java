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
import com.iktakademija.ednevnik.entities.StudentEntity;
import com.iktakademija.ednevnik.repositories.StudentRepository;

@RestController
@RequestMapping(value = "/api/v1/students")
public class StudentController {

	@Autowired
	private StudentRepository studentRepository;
	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage)
				.collect(Collectors.joining(" "));
	}
	
	// lista svih ucenika
	@RequestMapping(method = RequestMethod.GET, value = "")
	public ResponseEntity<?> getAllStudents() {
		List<StudentEntity> students = new ArrayList<>();
		students = (List<StudentEntity>) studentRepository.findAll();
		if (!students.isEmpty()) {
			return new ResponseEntity<List<StudentEntity>>(students, HttpStatus.OK);
		} else {
			return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Students not found"), HttpStatus.NOT_FOUND);
		}
	}
	
	// pronadji ucenika po id
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getStudentById(@PathVariable Integer id) {
		if (studentRepository.existsById(id)) {
			StudentEntity studentEntity = studentRepository.findById(id).get();
			return new ResponseEntity<StudentEntity>(studentEntity, HttpStatus.OK);
		} else {
			return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Student with id number " + id + " not found"), HttpStatus.NOT_FOUND);
		}
	}
	
	// dodaj novog ucenika
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createStudent(@RequestBody StudentEntity studentEntity,
			 BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		StudentEntity newStudent = new StudentEntity();
		newStudent.setName(studentEntity.getName());
		newStudent.setSurname(studentEntity.getSurname());
		newStudent.setEmail(studentEntity.getEmail());
		newStudent.setPassword(studentEntity.getPassword());
		newStudent.setUsername(studentEntity.getUsername());

		studentRepository.save(newStudent);
		return new ResponseEntity<StudentEntity>(newStudent, HttpStatus.CREATED);
	}
	
	// izmeni ucenika
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateStudent(@PathVariable Integer id, 
			@RequestBody StudentEntity updatedStudent, BindingResult result) {
		
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (studentRepository.existsById(id)) {
			StudentEntity studentEntity = studentRepository.findById(id).get();

			if (updatedStudent.getName() != null) {
				studentEntity.setName(updatedStudent.getName());
			}
			if (updatedStudent.getSurname() != null) {
				studentEntity.setSurname(updatedStudent.getSurname());
			}
			if (updatedStudent.getEmail() != null) {
				studentEntity.setEmail(updatedStudent.getEmail());
			}
			if (updatedStudent.getPassword() != null) {
				studentEntity.setPassword(updatedStudent.getPassword());
			}
			if (updatedStudent.getUsername() != null) {
				studentEntity.setUsername(updatedStudent.getUsername());
			}
		
			
			studentRepository.save(updatedStudent);
			return new ResponseEntity<StudentEntity>(updatedStudent, HttpStatus.OK);
		}
		else {
			return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Student with id number " + id + " not found"), HttpStatus.NOT_FOUND);
		}
	}
	
	// obrisi ucenika
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteStudent(@PathVariable Integer id) {
		if (studentRepository.existsById(id)) {
			studentRepository.deleteById(id);
			return new ResponseEntity<StudentEntity>(HttpStatus.OK);
		} else {
			return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Student with id number " + id + " not found"), HttpStatus.NOT_FOUND);
		}
	}
}

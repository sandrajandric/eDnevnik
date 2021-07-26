package com.iktakademija.ednevnik.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktakademija.ednevnik.controllers.util.RESTError;
import com.iktakademija.ednevnik.entities.ClassEntity;
import com.iktakademija.ednevnik.entities.SubjectEntity;
import com.iktakademija.ednevnik.entities.TeacherEntity;
import com.iktakademija.ednevnik.entities.TeacherSubjectEntity;
import com.iktakademija.ednevnik.entities.dto.UserDTO;
import com.iktakademija.ednevnik.repositories.SubjectRepository;
import com.iktakademija.ednevnik.repositories.TeacherRepository;
import com.iktakademija.ednevnik.repositories.TeacherSubjectRepository;

@RestController
@RequestMapping(value = "/api/v1/teachers")
public class TeacherController {

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private TeacherSubjectRepository teacherSubjectRepository;

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}

	// lista svih nastavnika
	@RequestMapping(method = RequestMethod.GET, value = "")
	public ResponseEntity<?> getAllTeachers() {
		List<TeacherEntity> teachers = new ArrayList<>();
		teachers = (List<TeacherEntity>) teacherRepository.findAll();
		if (!teachers.isEmpty()) {
			return new ResponseEntity<List<TeacherEntity>>(teachers, HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Teachers not found"),
					HttpStatus.NOT_FOUND);
		}
	}

	// pronadji nastavnika po id
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getTeacherById(@PathVariable Integer id) {
		if (teacherRepository.existsById(id)) {
			TeacherEntity teacherEntity = teacherRepository.findById(id).get();
			return new ResponseEntity<TeacherEntity>(teacherEntity, HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(), "Teacher with id number " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
	}

	// dodaj novog nastavnika
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createTeacher(@RequestBody UserDTO userDTO, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		TeacherEntity newTeacher = new TeacherEntity();
		newTeacher.setName(userDTO.getName());
		newTeacher.setSurname(userDTO.getSurname());
		newTeacher.setEmail(userDTO.getEmail());
		newTeacher.setPassword(userDTO.getPassword());
		newTeacher.setUsername(userDTO.getUsername());

		teacherRepository.save(newTeacher);
		return new ResponseEntity<TeacherEntity>(newTeacher, HttpStatus.CREATED);
	}

	// izmeni nastavnika
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateTeacher(@PathVariable Integer id, @RequestBody UserDTO userDTO,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (teacherRepository.existsById(id)) {
			TeacherEntity teacherEntity = teacherRepository.findById(id).get();

			if (userDTO.getName() != null) {
				teacherEntity.setName(userDTO.getName());
			}
			if (userDTO.getSurname() != null) {
				teacherEntity.setSurname(userDTO.getSurname());
			}
			if (userDTO.getEmail() != null) {
				teacherEntity.setEmail(userDTO.getEmail());
			}
			if (userDTO.getPassword() != null) {
				teacherEntity.setPassword(userDTO.getPassword());
			}
			if (userDTO.getUsername() != null) {
				teacherEntity.setUsername(userDTO.getUsername());
			}

			teacherRepository.save(teacherEntity);
			return new ResponseEntity<TeacherEntity>(teacherEntity, HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(), "Teacher with id number " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
	}

	// obrisi nastavnika
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteTeacher(@PathVariable Integer id) {
		if (teacherRepository.existsById(id)) {
			teacherRepository.deleteById(id);
			return new ResponseEntity<TeacherEntity>(HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(), "Teacher with id number " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
	}

	// dodaj predmet koji predaje
	@RequestMapping(method = RequestMethod.PUT, value = "/{teacherId}/assignSubjectToTeacher/{subjectId}")
	public ResponseEntity<?> assignSubjectsToTeacher(@PathVariable Integer teacherId, @PathVariable Integer subjectId) {
		if (teacherRepository.existsById(teacherId) && subjectRepository.existsById(subjectId)) {
			SubjectEntity subject = subjectRepository.findById(subjectId).get();
			TeacherEntity teacher = teacherRepository.findById(teacherId).get();
			TeacherSubjectEntity teacherSubject = new TeacherSubjectEntity();
			List<TeacherSubjectEntity> listOfSubjects = new ArrayList<>();
			if (!(teacherSubjectRepository.existsSubjectIdAndTeacherId(subjectId, teacherId) >= 1)) {
				teacherSubject.setSubject(subject);
				teacherSubject.setTeacher(teacher);
				listOfSubjects.add(teacherSubject);
				subject.setHasSubjects(listOfSubjects);
				
			} else {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.BAD_REQUEST.value(),
						"Subject with id number " + subjectId + " is already assigned to teacher with id number " + teacherId), HttpStatus.BAD_REQUEST);
			}
			
			teacherSubjectRepository.save(teacherSubject);
			return new ResponseEntity<TeacherSubjectEntity>(teacherSubject, HttpStatus.OK);
		} else {
			if (!subjectRepository.existsById(subjectId)) {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(),
						"Subject with id number " + subjectId + " not found"), HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(),
						"Teacher with id number " + teacherId + " not found"), HttpStatus.NOT_FOUND);
			}
		}
	}

	// obrisi predmet koji predaje
	@RequestMapping(method = RequestMethod.PUT, value = "/{teacherId}/removeSubjectFromTeacher/{subjectId}")
	public ResponseEntity<?> removeSubjectsFromTeacher(@PathVariable Integer teacherId,
			@PathVariable Integer subjectId) {
		if (teacherSubjectRepository.existsSubjectIdAndTeacherId(subjectId, teacherId) >= 1) {
			teacherSubjectRepository.removeSubjectFromTeacher(subjectId, teacherId);
			return new ResponseEntity<TeacherSubjectEntity>( HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(),
							"Subject with id number " + subjectId + " is not assigned to teacher " + teacherId),
					HttpStatus.NOT_FOUND);
		}
	}
}

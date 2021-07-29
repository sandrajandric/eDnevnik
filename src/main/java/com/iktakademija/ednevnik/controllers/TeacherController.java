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
import com.iktakademija.ednevnik.entities.RoleEntity;
import com.iktakademija.ednevnik.entities.SubjectEntity;
import com.iktakademija.ednevnik.entities.TeacherEntity;
import com.iktakademija.ednevnik.entities.TeacherSubjectEntity;
import com.iktakademija.ednevnik.entities.dto.UserDTO;
import com.iktakademija.ednevnik.entities.dto.UserDTOPut;
import com.iktakademija.ednevnik.repositories.RoleRepository;
import com.iktakademija.ednevnik.repositories.SubjectRepository;
import com.iktakademija.ednevnik.repositories.TeacherRepository;
import com.iktakademija.ednevnik.repositories.TeacherSubjectRepository;
import com.iktakademija.ednevnik.util.Encryption;

@RestController
@RequestMapping(value = "/api/v1/teachers")
@Secured("ROLE_ADMIN")
public class TeacherController {
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private TeacherSubjectRepository teacherSubjectRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	private Encryption encryption;

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}

	// lista svih nastavnika
	@RequestMapping(method = RequestMethod.GET, value = "")
	public ResponseEntity<?> getAllTeachers() {
		List<TeacherEntity> teachers = new ArrayList<>();
		teachers = (List<TeacherEntity>) teacherRepository.findAll();
		if (!teachers.isEmpty()) {
			logger.info("Viewed all teachers.");
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
			logger.info("Viewed teacher with id number " + id);
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
		
		RoleEntity role = roleRepository.findById(1).get();
		TeacherEntity newTeacher = new TeacherEntity();
		newTeacher.setName(userDTO.getName());
		newTeacher.setSurname(userDTO.getSurname());
		newTeacher.setEmail(userDTO.getEmail());
		newTeacher.setPassword(encryption.getPassEncoded(userDTO.getPassword()));
		newTeacher.setUsername(userDTO.getUsername());
		newTeacher.setRole(role);

		teacherRepository.save(newTeacher);
		logger.info("Created teacher " + newTeacher.toString());
		return new ResponseEntity<TeacherEntity>(newTeacher, HttpStatus.CREATED);
	}

	// izmeni nastavnika
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateTeacher(@PathVariable Integer id, @RequestBody UserDTOPut userDTO,
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
				teacherEntity.setPassword(encryption.getPassEncoded(userDTO.getPassword()));
			}
			if (userDTO.getUsername() != null) {
				teacherEntity.setUsername(userDTO.getUsername());
			}

			teacherRepository.save(teacherEntity);
			logger.info("Updated teacher " + teacherEntity.toString());
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
			logger.info("Deleted teacher with id number " + id);
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
			List<TeacherSubjectEntity> listTeacherSubject = new ArrayList<>();
			if (!(teacherSubjectRepository.existsSubjectIdAndTeacherId(subjectId, teacherId) >= 1)) {
				teacherSubject.setSubject(subject);
				teacherSubject.setTeacher(teacher);
				listTeacherSubject.add(teacherSubject);
				subject.setHasTeachers(listTeacherSubject);
				
			} else {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.BAD_REQUEST.value(),
						"Subject with id number " + subjectId + " is already assigned to teacher with id number " + teacherId), HttpStatus.BAD_REQUEST);
			}
			
			teacherSubjectRepository.save(teacherSubject);
			logger.info("Subject with id number " + subjectId + " added to teacher with id number " + teacherId);
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
			logger.info("Subject with id number " + subjectId + " removed from teacher with id number " + teacherId);
			return new ResponseEntity<TeacherSubjectEntity>( HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(),
							"Subject with id number " + subjectId + " is not assigned to teacher " + teacherId),
					HttpStatus.NOT_FOUND);
		}
	}
}

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
import com.iktakademija.ednevnik.entities.ParentEntity;
import com.iktakademija.ednevnik.entities.RoleEntity;
import com.iktakademija.ednevnik.entities.StudentEntity;
import com.iktakademija.ednevnik.entities.StudentTeacherSubjectEntity;
import com.iktakademija.ednevnik.entities.SubjectEntity;
import com.iktakademija.ednevnik.entities.TeacherEntity;
import com.iktakademija.ednevnik.entities.TeacherSubjectEntity;
import com.iktakademija.ednevnik.entities.dto.UserDTO;
import com.iktakademija.ednevnik.entities.dto.UserDTOPut;
import com.iktakademija.ednevnik.repositories.ParentRepository;
import com.iktakademija.ednevnik.repositories.RoleRepository;
import com.iktakademija.ednevnik.repositories.StudentRepository;
import com.iktakademija.ednevnik.repositories.StudentTeacherSubjectRepository;
import com.iktakademija.ednevnik.repositories.SubjectRepository;
import com.iktakademija.ednevnik.repositories.TeacherSubjectRepository;
import com.iktakademija.ednevnik.services.StudentTeacherSubjectService;
import com.iktakademija.ednevnik.util.Encryption;

@RestController
@RequestMapping(value = "/api/v1/students")
@Secured("ROLE_ADMIN")
public class StudentController {

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private ParentRepository parentRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private StudentTeacherSubjectRepository studentTeacherSubjectRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private TeacherSubjectRepository teacherSubjectRepository;

	@Autowired
	private StudentTeacherSubjectService studentTeacherSubjectService;

	private Encryption encryption;

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}

	// lista svih ucenika
	@RequestMapping(method = RequestMethod.GET, value = "")
	public ResponseEntity<?> getAllStudents() {
		List<StudentEntity> students = new ArrayList<>();
		students = (List<StudentEntity>) studentRepository.findAll();
		if (!students.isEmpty()) {
			logger.info("Viewed all students.");
			return new ResponseEntity<List<StudentEntity>>(students, HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Students not found"),
					HttpStatus.NOT_FOUND);
		}
	}

	// pronadji ucenika po id
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getStudentById(@PathVariable Integer id) {
		if (studentRepository.existsById(id)) {
			StudentEntity studentEntity = studentRepository.findById(id).get();
			logger.info("Viewed student with id number " + id);
			return new ResponseEntity<StudentEntity>(studentEntity, HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(), "Student with id number " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
	}

	// dodaj novog ucenika
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createStudent(@RequestBody UserDTO userDTO, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		RoleEntity role = roleRepository.findById(2).get();
		StudentEntity newStudent = new StudentEntity();
		newStudent.setName(userDTO.getName());
		newStudent.setSurname(userDTO.getSurname());
		newStudent.setEmail(userDTO.getEmail());
		newStudent.setPassword(encryption.getPassEncoded(userDTO.getPassword()));
		newStudent.setUsername(userDTO.getUsername());
		newStudent.setRole(role);

		studentRepository.save(newStudent);
		logger.info("Created student " + newStudent.toString());
		return new ResponseEntity<StudentEntity>(newStudent, HttpStatus.CREATED);
	}

	// izmeni ucenika
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateStudent(@PathVariable Integer id, @RequestBody UserDTOPut userDTO,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (studentRepository.existsById(id)) {
			StudentEntity studentEntity = studentRepository.findById(id).get();

			if (userDTO.getName() != null) {
				studentEntity.setName(userDTO.getName());
			}
			if (userDTO.getSurname() != null) {
				studentEntity.setSurname(userDTO.getSurname());
			}
			if (userDTO.getEmail() != null) {
				studentEntity.setEmail(userDTO.getEmail());
			}
			if (userDTO.getPassword() != null) {
				studentEntity.setPassword(encryption.getPassEncoded(userDTO.getPassword()));
			}
			if (userDTO.getUsername() != null) {
				studentEntity.setUsername(userDTO.getUsername());
			}

			studentRepository.save(studentEntity);
			logger.info("Updated student " + studentEntity.toString());
			return new ResponseEntity<StudentEntity>(studentEntity, HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(), "Student with id number " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
	}

	// obrisi ucenika
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteStudent(@PathVariable Integer id) {
		if (studentRepository.existsById(id)) {
			studentRepository.deleteById(id);
			logger.info("Deleted student with id number " + id);
			return new ResponseEntity<StudentEntity>(HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(), "Student with id number " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
	}

	// dodaj roditelja
	@RequestMapping(method = RequestMethod.PUT, value = "/{studentId}/assignParent/{parentId}")
	public ResponseEntity<?> assignParent(@PathVariable Integer parentId, @PathVariable Integer studentId) {
		if (parentRepository.existsById(parentId) && studentRepository.existsById(studentId)) {
			ParentEntity parent = parentRepository.findById(parentId).get();
			StudentEntity student = studentRepository.findById(studentId).get();
			List<StudentEntity> students = new ArrayList<>();
			if (!(studentRepository.existsParentStudent(parentId, studentId) >= 1)) {
				student.setParent(parent);
				students.add(student);
				parent.setStudents(students);
			} else {
				return new ResponseEntity<RESTError>(
						new RESTError(HttpStatus.BAD_REQUEST.value(), "Parent with id number " + parentId
								+ " is already assigned to student with id number " + studentId),
						HttpStatus.BAD_REQUEST);
			}
			parentRepository.save(parent);
			studentRepository.save(student);
			logger.info("Student with id number " + studentId + " added to parent with id number " + parentId);
			return new ResponseEntity<ParentEntity>(parent, HttpStatus.OK);
		} else {
			if (!studentRepository.existsById(studentId)) {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(),
						"Student with id number " + studentId + " not found"), HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<RESTError>(
						new RESTError(HttpStatus.NOT_FOUND.value(), "Parent with id number " + parentId + " not found"),
						HttpStatus.NOT_FOUND);
			}
		}
	}

	// obrisi roditelja
	@RequestMapping(method = RequestMethod.PUT, value = "/{studentId}/removeParentFromStudent/{parentId}")
	public ResponseEntity<?> removeParentFromStudent(@PathVariable Integer studentId, @PathVariable Integer parentId) {
		if (studentRepository.existsParentStudent(parentId, studentId) >= 1) {
			StudentEntity student = studentRepository.findById(studentId).get();
			student.setParent(null);
			studentRepository.save(student);
			logger.info("Parent with id number " + parentId + " removed from student with id number " + studentId);
			return new ResponseEntity<StudentEntity>(student, HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(),
							"Parent with id number " + parentId + " is not assigned to student " + studentId),
					HttpStatus.NOT_FOUND);
		}
	}

	// dodaj predmet
	@RequestMapping(method = RequestMethod.PUT, value = "/{studentId}/assignSubjectToStudent/{subjectId}")
	public ResponseEntity<?> assignSubjectToStudent(@PathVariable Integer studentId, @PathVariable Integer subjectId) {
		if (studentRepository.existsById(studentId) && subjectRepository.existsById(subjectId)) {
			SubjectEntity subject = subjectRepository.findById(subjectId).get();
			StudentEntity student = studentRepository.findById(studentId).get();
			TeacherSubjectEntity teacherSubject = teacherSubjectRepository.findBySubjectId(subjectId);
			StudentTeacherSubjectEntity studentTeacherSubject = new StudentTeacherSubjectEntity();
			studentTeacherSubject.setStudentt(student);
			studentTeacherSubject.setSubjectt(teacherSubject);

			studentTeacherSubjectRepository.save(studentTeacherSubject);
			logger.info("Subject with id number " + subjectId + " added to student with id number " + studentId);
			return new ResponseEntity<StudentTeacherSubjectEntity>(studentTeacherSubject, HttpStatus.OK);

		} else {
		if (!studentRepository.existsById(studentId)) {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(), "Student with id number " + studentId + " not found"),
					HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(), "Subject with id number " + subjectId + " not found"),
					HttpStatus.NOT_FOUND);
		}
	}
	}

	// obrisi predmet
	@RequestMapping(method = RequestMethod.PUT, value = "/{studentId}/removeSubjectFromStudent/{subjectId}")
	public ResponseEntity<?> removeSubjectFromStudent(@PathVariable Integer studentId,
			@PathVariable Integer subjectId) {
		if (studentRepository.existsById(studentId) && subjectRepository.existsById(subjectId)) {
			if (studentTeacherSubjectService.existsByStudentAndSubject(studentId, subjectId) >= 1) {
				studentTeacherSubjectService.removeSubjectFromStudent(studentId, subjectId);
				logger.info(
						"Subject with id number " + subjectId + " removed from student with id number " + studentId);
				return new ResponseEntity<StudentTeacherSubjectEntity>(HttpStatus.OK);
			} else {
				return new ResponseEntity<RESTError>(
						new RESTError(HttpStatus.NOT_FOUND.value(),
								"Subject with id number " + subjectId + " is not assigned to student " + studentId),
						HttpStatus.NOT_FOUND);
			}
		} else {
			if (!studentRepository.existsById(studentId)) {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(),
						"Student with id number " + studentId + " not found"), HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(),
						"Subject with id number " + subjectId + " not found"), HttpStatus.NOT_FOUND);
			}
		}

	}
}

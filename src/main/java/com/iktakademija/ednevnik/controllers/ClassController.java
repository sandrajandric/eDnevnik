package com.iktakademija.ednevnik.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
import com.iktakademija.ednevnik.entities.ClassEntity;
import com.iktakademija.ednevnik.entities.StudentEntity;
import com.iktakademija.ednevnik.entities.TeacherEntity;
import com.iktakademija.ednevnik.entities.dto.ClassDTO;
import com.iktakademija.ednevnik.entities.dto.ClassDTOPut;
import com.iktakademija.ednevnik.entities.enums.EYear;
import com.iktakademija.ednevnik.repositories.ClassRepository;
import com.iktakademija.ednevnik.repositories.StudentRepository;
import com.iktakademija.ednevnik.repositories.TeacherRepository;

@RestController
@RequestMapping(value = "/api/v1/classes")
@Secured("ROLE_ADMIN")
public class ClassController {

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ClassRepository classRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}

	// lista svih odeljenja
	@RequestMapping(method = RequestMethod.GET, value = "")
	public ResponseEntity<?> getAllClasses() {
		List<ClassEntity> classes = new ArrayList<>();
		classes = (List<ClassEntity>) classRepository.findAll();
		if (!classes.isEmpty()) {
			logger.info("Viewed all classes.");
			return new ResponseEntity<List<ClassEntity>>(classes, HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Classes not found"),
					HttpStatus.NOT_FOUND);
		}
	}

	// pronadji odeljenje po id
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getClassById(@PathVariable Integer id) {
		if (classRepository.existsById(id)) {
			ClassEntity classEntity = classRepository.findById(id).get();
			logger.info("Viewed class with id number " + id);
			return new ResponseEntity<ClassEntity>(classEntity, HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(), "Class with id number " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
	}

	// dodaj novo odeljenje
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createClass(@Valid @RequestBody ClassDTO classDTO, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		ClassEntity newClass = new ClassEntity();

		if (classRepository.existsByClassNumberAndYear(classDTO.getClassNumber(), classDTO.getYear())) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.BAD_REQUEST.value(),
					"Class " + EYear.valueOf(classDTO.getYear().toString()) + " " + classDTO.getClassNumber()
							+ " already exists."),
					HttpStatus.NOT_FOUND);
		} else {
			newClass.setClassNumber(classDTO.getClassNumber());
			newClass.setYear(EYear.valueOf(classDTO.getYear().toString()));

			classRepository.save(newClass);
			logger.info("Created class " + newClass.toString());
			return new ResponseEntity<ClassEntity>(newClass, HttpStatus.CREATED);
		}

	}

	// izmeni odeljenje
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateClass(@PathVariable Integer id, @Valid @RequestBody ClassDTOPut classDTO,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		if (classRepository.existsById(id)) {
			ClassEntity classEntity = classRepository.findById(id).get();

			if (classRepository.existsByClassNumberAndYear(classDTO.getClassNumber(), classDTO.getYear())) {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.BAD_REQUEST.value(),
						"Class " + EYear.valueOf(classDTO.getYear().toString()) + " " + classDTO.getClassNumber()
								+ " already exists."),
						HttpStatus.NOT_FOUND);
			} else {
				if (classDTO.getClassNumber() != null) {
					classEntity.setClassNumber(classDTO.getClassNumber());
				}
				if (classDTO.getYear() != null) {
					classEntity.setYear(classDTO.getYear());
				}

				classRepository.save(classEntity);
				logger.info("Updated class with id number " + id);
				return new ResponseEntity<ClassEntity>(classEntity, HttpStatus.OK);
			}
		}

		return new ResponseEntity<RESTError>(
				new RESTError(HttpStatus.NOT_FOUND.value(), "Class with id number " + id + " not found"),
				HttpStatus.NOT_FOUND);

	}

	// obrisi odeljenje
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteClass(@PathVariable Integer id) {

		if (classRepository.existsById(id)) {
			classRepository.deleteById(id);
			logger.info("Deleted class with id number " + id);
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(), "Class with id number " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
	}

	// dodaj ucenika u odeljenje
	@RequestMapping(method = RequestMethod.PUT, value = "/{classId}/assignStudent/{studentId}")
	public ResponseEntity<?> addStudentToClass(@PathVariable Integer classId, 
			@PathVariable Integer studentId) {		
		
		if (classRepository.existsById(classId) && studentRepository.existsById(studentId)) {
			if (!(studentRepository.existsClass(classId, studentId) >= 1)) {
				ClassEntity classs= classRepository.findById(classId).get();
				StudentEntity student = studentRepository.findById(studentId).get();
				List<StudentEntity> students = new ArrayList<>();
				student.setClasss(classs);
				students.add(student);
				classs.setStudents(students);
				classRepository.save(classs);
				studentRepository.save(student);
				logger.info("Student with id number " + studentId + " added to class with id number " + classId);
				return new ResponseEntity<ClassEntity>(classs, HttpStatus.OK);

			} else {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.BAD_REQUEST.value(),
						"Student with id number " + studentId + " is already assigned to class with id number " + classId), HttpStatus.BAD_REQUEST);
			}
		} else {
			if (!classRepository.existsById(classId)) {
				return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Class with id number " + classId+ " not found"), HttpStatus.NOT_FOUND);
			} else {
				return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Student with id number " + studentId + " not found"), HttpStatus.NOT_FOUND);
			}
		}
	}

	// obrisi ucenika iz odeljenja
	@RequestMapping(method = RequestMethod.PUT, value = "/{classId}/removeStudent/{studentId}")
	public ResponseEntity<?> removeStudentFromClass(@PathVariable Integer classId, @PathVariable Integer studentId) {

		if (classRepository.existsById(classId) && studentRepository.existsById(studentId)) {
			ClassEntity classs = classRepository.findById(classId).get();
			StudentEntity student = studentRepository.findById(studentId).get();
			List<StudentEntity> students = (List<StudentEntity>) studentRepository.findAll();
			students.remove(student);
			List<StudentEntity> studentsNew = (List<StudentEntity>) studentRepository.findAll();
			student.setClasss(null);
			classs.setStudents(studentsNew);
			classRepository.save(classs);
			studentRepository.save(student);
			logger.info("Student with id number " + studentId + " removed from class with id number " + classId);
			return new ResponseEntity<ClassEntity>(classs, HttpStatus.OK);
		} else {
			if (!classRepository.existsById(classId)) {
				return new ResponseEntity<RESTError>(
						new RESTError(HttpStatus.NOT_FOUND.value(), "Class with id number " + classId + " not found"),
						HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(),
						"Student with id number " + studentId + " not found"), HttpStatus.NOT_FOUND);
			}
		}
	}

	// dodaj odeljenskog
	@RequestMapping(method = RequestMethod.PUT, value = "/{classId}/assignHomeroomTeacher/{teacherId}")
	public ResponseEntity<?> assignHomeroomTeacher(@PathVariable Integer classId, @PathVariable Integer teacherId) {

		if (classRepository.existsById(classId) && teacherRepository.existsById(teacherId)) {
			ClassEntity classs = classRepository.findById(classId).get();
			TeacherEntity teacher = teacherRepository.findById(teacherId).get();
			classs.setHomeroomTeacher(teacher);
			teacher.setInChargeOfClass(classs);
			classRepository.save(classs);
			teacherRepository.save(teacher);
			logger.info("Teacher with id number " + teacherId + " added as homeroom teacher to class with id number "
					+ classId);
			return new ResponseEntity<ClassEntity>(classs, HttpStatus.OK);
		} else {
			if (!classRepository.existsById(classId)) {
				return new ResponseEntity<RESTError>(
						new RESTError(HttpStatus.NOT_FOUND.value(), "Class with id number " + classId + " not found"),
						HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(),
						"Teacher with id number " + teacherId + " not found"), HttpStatus.NOT_FOUND);
			}
		}
	}

	// obrisi odeljenskog
	@RequestMapping(method = RequestMethod.PUT, value = "/{classId}/removeHomeroomTeacher/{teacherId}")
	public ResponseEntity<?> removeHomeroomTeacherFromClass(@PathVariable Integer classId,
			@PathVariable Integer teacherId) {

		if (classRepository.existsById(classId) && teacherRepository.existsById(teacherId)) {
			ClassEntity classs = classRepository.findById(classId).get();
			TeacherEntity teacher = teacherRepository.findById(teacherId).get();
			teacher.setInChargeOfClass(null);
			classs.setHomeroomTeacher(null);
			classRepository.save(classs);
			teacherRepository.save(teacher);
			logger.info("Teacher with id number " + teacherId + " removed as homeroom teacher for class with id number "
					+ classId);
			return new ResponseEntity<ClassEntity>(classs, HttpStatus.OK);
		} else {
			if (!classRepository.existsById(classId)) {
				return new ResponseEntity<RESTError>(
						new RESTError(HttpStatus.NOT_FOUND.value(), "Class with id number " + classId + " not found"),
						HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(),
						"Teacher with id number " + teacherId + " not found"), HttpStatus.NOT_FOUND);
			}
		}
	}

}

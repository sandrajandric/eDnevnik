package com.iktakademija.ednevnik.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

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
import com.iktakademija.ednevnik.entities.ClassEntity;
import com.iktakademija.ednevnik.entities.StudentEntity;
import com.iktakademija.ednevnik.entities.SubjectEntity;
import com.iktakademija.ednevnik.entities.TeacherEntity;
import com.iktakademija.ednevnik.entities.TeacherSubjectEntity;
import com.iktakademija.ednevnik.entities.TeacherSubjectStudentEntity;
import com.iktakademija.ednevnik.entities.enums.EYear;
import com.iktakademija.ednevnik.repositories.ClassRepository;
import com.iktakademija.ednevnik.repositories.StudentRepository;
import com.iktakademija.ednevnik.repositories.SubjectRepository;
import com.iktakademija.ednevnik.repositories.TeacherRepository;
import com.iktakademija.ednevnik.repositories.TeacherSubjectStudentRepository;

@RestController
@RequestMapping(value = "/api/v1/classes")
public class ClassController {

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
			return new ResponseEntity<ClassEntity>(classEntity, HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(), "Class with id number " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
	}

	// dodaj novo odeljenje
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createClass(@RequestBody ClassEntity classEntity, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		ClassEntity newClass = new ClassEntity();

		if (classRepository.existsByClassNumberAndYear(classEntity.getClassNumber(), classEntity.getYear())) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.BAD_REQUEST.value(),
					"Class " + EYear.valueOf(classEntity.getYear().toString()) + " " + classEntity.getClassNumber()
							+ " already exists."),
					HttpStatus.NOT_FOUND);
		} else {
			newClass.setClassNumber(classEntity.getClassNumber());
			newClass.setYear(EYear.valueOf(classEntity.getYear().toString()));
			newClass.setHomeroomTeacher(classEntity.getHomeroomTeacher());

			classRepository.save(newClass);
			return new ResponseEntity<ClassEntity>(newClass, HttpStatus.CREATED);
		}

	}

	// izmeni odeljenje
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateClass(@PathVariable Integer id, 
			@RequestBody ClassEntity updatedClass, BindingResult result) {
		
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (classRepository.existsById(id)) {
			ClassEntity classEntity = classRepository.findById(id).get();

			if (classRepository.existsByClassNumberAndYear(updatedClass.getClassNumber(), updatedClass.getYear())) {
				return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.BAD_REQUEST.value(), "Class "
						+ EYear.valueOf(updatedClass.getYear().toString()) + " " + updatedClass.getClassNumber() + " already exists."), HttpStatus.NOT_FOUND);
			} else {
				if (updatedClass.getClassNumber() != null) {
					classEntity.setClassNumber(updatedClass.getClassNumber());
				}
				if (updatedClass.getYear() != null) {
					classEntity.setYear(updatedClass.getYear());
				}
				
				classRepository.save(updatedClass);
				return new ResponseEntity<ClassEntity>(updatedClass, HttpStatus.OK);
			}
		}

	return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(),"Class with id number "+id+" not found"),HttpStatus.NOT_FOUND);

	}

	// obrisi odeljenje
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteClass(@PathVariable Integer id) {
		if (classRepository.existsById(id)) {
			classRepository.deleteById(id);
			classRepository.save(classRepository.findById(id).get());
			return new ResponseEntity<ClassEntity>(HttpStatus.OK);
		} else {
			return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Class with id number " + id + " not found"), HttpStatus.NOT_FOUND);
		}
	}

	// dodaj ucenika u odeljenje
	@RequestMapping(method = RequestMethod.PUT, value = "/{classId}/assignStudent/{studentId}")
	public ResponseEntity<?> addStudentToClass(@PathVariable Integer classId, 
			@PathVariable Integer studentId) {
		if (classRepository.existsById(classId) && studentRepository.existsById(studentId)) {
			ClassEntity classs = classRepository.findById(classId).get();
			StudentEntity student = studentRepository.findById(studentId).get();
			List<StudentEntity> students = new ArrayList<>();
			student.setClasss(classs);
			students.add(student);
			classs.setStudents(students);
			classRepository.save(classs);
			studentRepository.save(student);
			return new ResponseEntity<ClassEntity>(classs, HttpStatus.OK);
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
	public ResponseEntity<?> removeStudentFromClass(@PathVariable Integer classId, 
			@PathVariable Integer studentId) {
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
			return new ResponseEntity<ClassEntity>(classs, HttpStatus.OK);
		} else {
			if (!classRepository.existsById(classId)) {
				return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Class with id number " + classId+ " not found"), HttpStatus.NOT_FOUND);
			} else {
				return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Student with id number " + studentId + " not found"), HttpStatus.NOT_FOUND);
			}
		}
	}

	// dodaj odeljenskog
	@RequestMapping(method = RequestMethod.PUT, value = "/{classId}/assignHomeroomTeacher/{teacherId}")
	public ResponseEntity<?> assignHomeroomTeacher(@PathVariable Integer classId, 
			@PathVariable Integer teacherId) {
		if (classRepository.existsById(classId) && teacherRepository.existsById(teacherId)) {
			ClassEntity classs = classRepository.findById(classId).get();
			TeacherEntity teacher = teacherRepository.findById(teacherId).get();
			classs.setHomeroomTeacher(teacher);
			teacher.setInChargeOfClass(classs);
			classRepository.save(classs);
			teacherRepository.save(teacher);
			return new ResponseEntity<ClassEntity>(classs, HttpStatus.OK);
		} else {
			if (!classRepository.existsById(classId)) {
				return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Class with id number " + classId + " not found"), HttpStatus.NOT_FOUND);
			} else {
				return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Teacher with id number " + teacherId + " not found"), HttpStatus.NOT_FOUND);
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
			return new ResponseEntity<ClassEntity>(classs, HttpStatus.OK);
		} else {
			if (!classRepository.existsById(classId)) {
				return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Class with id number " + classId+ " not found"), HttpStatus.NOT_FOUND);
			} else {
				return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Teacher with id number " + teacherId + " not found"), HttpStatus.NOT_FOUND);
			}
		}
	}

}

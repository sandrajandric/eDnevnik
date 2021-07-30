package com.iktakademija.ednevnik.controllers;

import java.time.LocalDate;
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
import com.iktakademija.ednevnik.entities.GradeEntity;
import com.iktakademija.ednevnik.entities.StudentEntity;
import com.iktakademija.ednevnik.entities.SubjectEntity;
import com.iktakademija.ednevnik.entities.TeacherEntity;
import com.iktakademija.ednevnik.entities.TeacherSubjectEntity;
import com.iktakademija.ednevnik.entities.Grade;
import com.iktakademija.ednevnik.entities.UserEntity;
import com.iktakademija.ednevnik.entities.dto.GradeDTO;
import com.iktakademija.ednevnik.repositories.GradeRepository;
import com.iktakademija.ednevnik.repositories.StudentRepository;
import com.iktakademija.ednevnik.repositories.SubjectRepository;
import com.iktakademija.ednevnik.repositories.TeacherRepository;
import com.iktakademija.ednevnik.repositories.TeacherSubjectRepository;
import com.iktakademija.ednevnik.repositories.TeacherSubjectStudentRepository;
import com.iktakademija.ednevnik.repositories.UserRepository;
import com.iktakademija.ednevnik.services.EmailService;
import com.iktakademija.ednevnik.services.GradeService;
import com.iktakademija.ednevnik.services.TeacherSubjectStudentService;
import com.iktakademija.ednevnik.services.UserService;
import com.iktakademija.ednevnik.services.dto.EmailObject;

@RestController
@RequestMapping(value = "/api/v1/grades")
@Secured("ROLE_ADMIN")
public class GradeController {

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GradeRepository gradeRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private TeacherSubjectRepository teacherSubjectRepository;

	@Autowired
	private TeacherSubjectStudentRepository teacherSubjectStudentRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private GradeService gradeService;

	@Autowired
	private UserService userService;

	@Autowired
	private TeacherSubjectStudentService tesubSubjectStudentService;

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "));
	}

	// lista svih ocena
	@RequestMapping(method = RequestMethod.GET, value = "")
	public ResponseEntity<?> getAllGrades() {
		List<GradeEntity> grades = new ArrayList<>();
		grades = (List<GradeEntity>) gradeRepository.findAll();
		if (!grades.isEmpty()) {
			logger.info("Viewed all grades.");
			return new ResponseEntity<List<GradeEntity>>(grades, HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Grades not found"),
					HttpStatus.NOT_FOUND);
		}
	}

	// pronadji ocenu po id
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getGradeById(@PathVariable Integer id) {
		if (gradeRepository.existsById(id)) {
			GradeEntity gradeEntity = gradeRepository.findById(id).get();
			gradeEntity.toString();
			logger.info("Viewed grade: " + gradeEntity.toString());
			return new ResponseEntity<GradeEntity>(gradeEntity, HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(), "Grade with id number " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
	}

	// dodeli ocenu
	@RequestMapping(method = RequestMethod.POST, value = "{teacherId}/addNewGradeToStudent/{studentId}/forSubject/{subjectId}")
	@Secured({ "ROLE_TEACHER", "ROLE_ADMIN" })
	public ResponseEntity<?> addNewGradeToStudent(@PathVariable Integer teacherId, @PathVariable Integer studentId,
			@PathVariable Integer subjectId, @Valid @RequestBody GradeDTO gradeDTO, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		GradeEntity newGrade = new GradeEntity();
		SubjectEntity subject = subjectRepository.findById(subjectId).get();
		StudentEntity student = studentRepository.findById(studentId).get();
		TeacherSubjectEntity teacherSubject = teacherSubjectRepository.findBySubjectIdAndTeacherId(subjectId,
				teacherId);
		Grade tsse = new Grade();

		if (studentRepository.existsById(studentId)) {
			if (teacherRepository.existsById(teacherId)) {
				if (subjectRepository.existsById(subjectId)) {
					if (teacherSubjectRepository.existsSubjectIdAndTeacherId(subjectId, teacherId) >= 1) {
						if (tesubSubjectStudentService.existsTeacherSubjectStudent(teacherId, subjectId,
								studentId) >= 1) {
							if (subject.getSubjectForYear().equals(student.getClasss().getYear())) {
								tsse.setStudent(student);
								tsse.setTeacherSubject(teacherSubject);
								newGrade.setGrade(gradeDTO.getGrade());
								newGrade.setGradeType(gradeDTO.getGradeType());
								newGrade.setDate(LocalDate.now());
								newGrade.setHasGrades(tsse);

								teacherSubjectStudentRepository.save(tsse);
								gradeRepository.save(newGrade);
								logger.info("Created grade " + newGrade.toString());

								if (tsse.getStudent().getParent().getEmail() != null) {
									EmailObject emailObject = new EmailObject();
									emailObject.setTo(tsse.getStudent().getParent().getEmail());
									emailObject.setSubject("New grade has been added");
									emailObject.setText(newGrade.toString());
									emailService.sendSimpleMessage(emailObject);

									logger.info("Email with grade id " + newGrade.getId()
											+ " has been sent to parent email "
											+ tsse.getStudent().getParent().getEmail());
								} else {
									return new ResponseEntity<RESTError>(
											new RESTError(HttpStatus.NOT_FOUND.value(), "Email not found"),
											HttpStatus.NOT_FOUND);
								}
								return new ResponseEntity<GradeEntity>(newGrade, HttpStatus.CREATED);
							} else {
								return new ResponseEntity<RESTError>(
										new RESTError(HttpStatus.BAD_REQUEST.value(),
												"Class year and subject for year have to be the same "),
										HttpStatus.BAD_REQUEST);
							}
						} else {
							return new ResponseEntity<RESTError>(
									new RESTError(HttpStatus.NOT_FOUND.value(),
											"Student with id number " + studentId
													+ " is not listening to subject with id number " + subjectId
													+ " and teacher with id number " + teacherId),
									HttpStatus.NOT_FOUND);
						}
					} else {
						return new ResponseEntity<RESTError>(
								new RESTError(HttpStatus.NOT_FOUND.value(),
										"Subject with id number " + subjectId
												+ " is not assigned to teacher with id number " + teacherId),
								HttpStatus.NOT_FOUND);
					}
				} else {
					return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(),
							"Subject with id number " + subjectId + " not found"), HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(),
						"Teacher with id number " + teacherId + " not found"), HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(), "Student with id number " + studentId + " not found"),
					HttpStatus.NOT_FOUND);
		}

	}

	// obrisi ocenu
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteGradeById(@PathVariable Integer id) {
		if (gradeRepository.existsById(id)) {
			gradeRepository.existsById(id);
			gradeRepository.deleteById(id);
			logger.info("Deleted grade with id number " + id);
			return new ResponseEntity<GradeEntity>(HttpStatus.OK);
		} else {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(), "Grade with id number " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
	}

	// lista svih ocena jednog studenta
	@RequestMapping(method = RequestMethod.GET, value = "/getAllGradesForStudent/{studentId}")
	@Secured({ "ROLE_STUDENT", "ROLE_PARENT", "ROLE_ADMIN" })
	public ResponseEntity<?> getAllGradesForStudent(@PathVariable Integer studentId) {

		StudentEntity student = studentRepository.findById(studentId).get();
		UserEntity user = userRepository.findById(1).get();

		if (studentRepository.existsById(studentId)) {
			String username = userService.getLoggedUser();
			if (username.equals(student.getUsername()) || username.equals(student.getParent().getUsername())
					|| username.equals(user.getUsername())) {
				List<GradeEntity> grades = new ArrayList<>();
				grades = (List<GradeEntity>) gradeService.findAllGradesByStudent(studentId);
				if (!grades.isEmpty()) {
					logger.info("Viewed all grades for student with id " + studentId);
					return new ResponseEntity<List<GradeEntity>>(grades, HttpStatus.OK);
				} else {
					return new ResponseEntity<RESTError>(
							new RESTError(HttpStatus.NOT_FOUND.value(), "Grades not found"), HttpStatus.NOT_FOUND);
				}
			} else {
				return new ResponseEntity<RESTError>(
						new RESTError(HttpStatus.UNAUTHORIZED.value(),
								"You are unauthorized to view grades of student with id number " + studentId),
						HttpStatus.UNAUTHORIZED);
			}

		} else {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(), "Student with id number " + studentId + " not found"),
					HttpStatus.NOT_FOUND);
		}
	}

	// lista svih ocena i predmeta kojima predaje nastavnik
	@RequestMapping(method = RequestMethod.GET, value = "/{teacherId}/getAllGradesForStudent/{studentId}/forSubject/{subjectId}")
	@Secured({"ROLE_TEACHER", "ROLE_ADMIN"})
	public ResponseEntity<?> getAllGradesForSubject(@PathVariable Integer teacherId, @PathVariable Integer subjectId, @PathVariable Integer studentId) {

		String username = userService.getLoggedUser();
		
		UserEntity user = userRepository.findById(1).get();
		TeacherEntity teacher = teacherRepository.findById(teacherId).get();
		
		if (studentRepository.existsById(studentId)) {
			if (subjectRepository.existsById(subjectId)) {
				if (username.equals(teacher.getUsername()) || username.equals(user.getUsername())) {
					if (subjectRepository.existsById(subjectId)) {
						if (teacherRepository.existsById(teacherId)) {
							if (teacherSubjectRepository.existsSubjectIdAndTeacherId(subjectId, teacherId) >= 1) {
								if (tesubSubjectStudentService.existsTeacherSubjectStudent(teacherId, subjectId,
										studentId) >= 1) {
								} else {
									return new ResponseEntity<RESTError>(
											new RESTError(HttpStatus.NOT_FOUND.value(), "Student with id number " + studentId
															+ " is not listening to subject with id number " + subjectId
															+ " and teacher with id number " + teacherId),
											HttpStatus.NOT_FOUND);
								}
								List<GradeEntity> grades = new ArrayList<>();
								grades = (List<GradeEntity>) gradeService.findAllGradesByTeacherSubjectStudent(subjectId, teacherId, studentId);
								if (!grades.isEmpty()) {
									logger.info("Viewed all grades for subject with id " + subjectId);
									return new ResponseEntity<List<GradeEntity>>(grades, HttpStatus.OK);
								} else {
									return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Grades not found")
											, HttpStatus.NOT_FOUND);
								}
							} else {
								return new ResponseEntity<RESTError>(
										new RESTError(HttpStatus.NOT_FOUND.value(), "Subject with id number " + subjectId
											+ " is not assigned to teacher with id number " + teacher.getId()), HttpStatus.NOT_FOUND);
							}
						} else {
							return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Teacher not found"),
									HttpStatus.NOT_FOUND);
						}
					} else {
						return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Subject with id number " + subjectId + " not found"),
								HttpStatus.NOT_FOUND);
					}
					
					
				} else {
					return new ResponseEntity<RESTError>(new RESTError(HttpStatus.UNAUTHORIZED.value(), "You are unauthorized to view grades for subject with id number " + subjectId ),
							HttpStatus.UNAUTHORIZED);
				}
				
			} else {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Subject with id number " + subjectId + " not found"),
						HttpStatus.NOT_FOUND);
			}
			
		} else {
			return new ResponseEntity<RESTError>(
					new RESTError(HttpStatus.NOT_FOUND.value(), "Student with id number " + studentId + " not found"),
					HttpStatus.NOT_FOUND);
		}
		
	}
}

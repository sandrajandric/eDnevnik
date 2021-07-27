package com.iktakademija.ednevnik.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktakademija.ednevnik.controllers.util.RESTError;
import com.iktakademija.ednevnik.entities.ClassEntity;
import com.iktakademija.ednevnik.entities.ParentEntity;
import com.iktakademija.ednevnik.entities.StudentEntity;
import com.iktakademija.ednevnik.entities.SubjectEntity;
import com.iktakademija.ednevnik.entities.TeacherEntity;
import com.iktakademija.ednevnik.entities.TeacherSubjectEntity;
import com.iktakademija.ednevnik.entities.dto.UserDTO;
import com.iktakademija.ednevnik.entities.dto.UserDTOPut;
import com.iktakademija.ednevnik.repositories.ParentRepository;
import com.iktakademija.ednevnik.repositories.StudentRepository;

@RestController
@RequestMapping(value = "/api/v1/parents")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ParentController {

	@Autowired
	private ParentRepository parentRepository;
	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage)
				.collect(Collectors.joining(" "));
	}
	
	// lista svih roditelja
	@RequestMapping(method = RequestMethod.GET, value = "")
	public ResponseEntity<?> getAllParents() {
		List<ParentEntity> parents = new ArrayList<>();
		parents = (List<ParentEntity>) parentRepository.findAll();
		if (!parents.isEmpty()) {
			return new ResponseEntity<List<ParentEntity>>(parents, HttpStatus.OK);
		} else {
			return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Parents not found"), HttpStatus.NOT_FOUND);
		}
	}
	
	// pronadji roditelja po id
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getParentById(@PathVariable Integer id) {
		if (parentRepository.existsById(id)) {
			ParentEntity parentEntity = parentRepository.findById(id).get();
			return new ResponseEntity<ParentEntity>(parentEntity, HttpStatus.OK);
		} else {
			return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Parent with id number " + id + " not found"), HttpStatus.NOT_FOUND);
		}
	}
	
	// dodaj novog roditelja
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createParent(@Valid @RequestBody UserDTO userDTO,
			 BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		ParentEntity newParent = new ParentEntity();
		newParent.setName(userDTO.getName());
		newParent.setSurname(userDTO.getSurname());
		newParent.setEmail(userDTO.getEmail());
		newParent.setPassword(userDTO.getPassword());
		newParent.setUsername(userDTO.getUsername());

		parentRepository.save(newParent);
		return new ResponseEntity<ParentEntity>(newParent, HttpStatus.CREATED);
	}
	
	// izmeni roditelja
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateParent(@PathVariable Integer id, 
			@Valid @RequestBody UserDTOPut  userDTO, BindingResult result) {
		
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (parentRepository.existsById(id)) {
			ParentEntity parentEntity = parentRepository.findById(id).get();

			if (userDTO.getName() != null) {
				parentEntity.setName(userDTO.getName());
			}
			if (userDTO.getSurname() != null) {
				parentEntity.setSurname(userDTO.getSurname());
			}
			if (userDTO.getEmail() != null) {
				parentEntity.setEmail(userDTO.getEmail());
			}
			if (userDTO.getPassword() != null) {
				parentEntity.setPassword(userDTO.getPassword());
			}
			if (userDTO.getUsername() != null) {
				parentEntity.setUsername(userDTO.getUsername());
			}
		
			
			parentRepository.save(parentEntity);
			return new ResponseEntity<ParentEntity>(parentEntity, HttpStatus.OK);
		}
		else {
			return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Parent with id number " + id + " not found"), HttpStatus.NOT_FOUND);
		}
	}
	
	// obrisi roditelja
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteParent(@PathVariable Integer id) {
		if (parentRepository.existsById(id)) {
			parentRepository.deleteById(id);
			return new ResponseEntity<ParentEntity>(HttpStatus.OK);
		} else {
			return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Parent with id number " + id + " not found"), HttpStatus.NOT_FOUND);
		}
	}
	
	
}

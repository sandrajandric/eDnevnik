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
import com.iktakademija.ednevnik.entities.ParentEntity;
import com.iktakademija.ednevnik.repositories.ParentRepository;

@RestController
@RequestMapping(value = "/api/v1/parents")
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
	public ResponseEntity<?> createParent(@RequestBody ParentEntity parentEntity,
			 BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		ParentEntity newParent = new ParentEntity();
		newParent.setName(parentEntity.getName());
		newParent.setSurname(parentEntity.getSurname());
		newParent.setEmail(parentEntity.getEmail());
		newParent.setPassword(parentEntity.getPassword());
		newParent.setUsername(parentEntity.getUsername());

		parentRepository.save(newParent);
		return new ResponseEntity<ParentEntity>(newParent, HttpStatus.CREATED);
	}
	
	// izmeni roditelja
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateParent(@PathVariable Integer id, 
			@RequestBody ParentEntity updatedParent, BindingResult result) {
		
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (parentRepository.existsById(id)) {
			ParentEntity parentEntity = parentRepository.findById(id).get();

			if (updatedParent.getName() != null) {
				parentEntity.setName(updatedParent.getName());
			}
			if (updatedParent.getSurname() != null) {
				parentEntity.setSurname(updatedParent.getSurname());
			}
			if (updatedParent.getEmail() != null) {
				parentEntity.setEmail(updatedParent.getEmail());
			}
			if (updatedParent.getPassword() != null) {
				parentEntity.setPassword(updatedParent.getPassword());
			}
			if (updatedParent.getUsername() != null) {
				parentEntity.setUsername(updatedParent.getUsername());
			}
		
			
			parentRepository.save(updatedParent);
			return new ResponseEntity<ParentEntity>(updatedParent, HttpStatus.OK);
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

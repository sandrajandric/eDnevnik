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
import com.iktakademija.ednevnik.entities.ParentEntity;
import com.iktakademija.ednevnik.entities.RoleEntity;
import com.iktakademija.ednevnik.entities.dto.UserDTO;
import com.iktakademija.ednevnik.entities.dto.UserDTOPut;
import com.iktakademija.ednevnik.repositories.ParentRepository;
import com.iktakademija.ednevnik.repositories.RoleRepository;
import com.iktakademija.ednevnik.util.Encryption;

@RestController
@RequestMapping(value = "/api/v1/parents")
@Secured("ROLE_ADMIN")
public class ParentController {
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ParentRepository parentRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	private Encryption encryption;
	
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
			logger.info("Viewed all parents.");
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
			logger.info("Viewed parent with id number " + id);
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

		RoleEntity role = roleRepository.findById(3).get();
		ParentEntity newParent = new ParentEntity();
		newParent.setName(userDTO.getName());
		newParent.setSurname(userDTO.getSurname());
		newParent.setEmail(userDTO.getEmail());
		newParent.setPassword(encryption.getPassEncoded(userDTO.getPassword()));
		newParent.setUsername(userDTO.getUsername());
		newParent.setRole(role);
		
		parentRepository.save(newParent);
		logger.info("Created parent " + newParent.toString());
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
				parentEntity.setPassword(encryption.getPassEncoded(userDTO.getPassword()));
			}
			if (userDTO.getUsername() != null) {
				parentEntity.setUsername(userDTO.getUsername());
			}
		
			
			parentRepository.save(parentEntity);
			logger.info("Updated parent " + parentEntity.toString());
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
			logger.info("Deleted parent with id number " + id);
			return new ResponseEntity<ParentEntity>(HttpStatus.OK);
		} else {
			return new  ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Parent with id number " + id + " not found"), HttpStatus.NOT_FOUND);
		}
	}
	
	
}

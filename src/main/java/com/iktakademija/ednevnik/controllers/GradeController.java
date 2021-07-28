package com.iktakademija.ednevnik.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/grades")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class GradeController {

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

}

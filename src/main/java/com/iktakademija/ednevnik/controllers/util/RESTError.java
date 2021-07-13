package com.iktakademija.ednevnik.controllers.util;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.ednevnik.security.Views;

public class RESTError {

	@JsonView(Views.Public.class)
	private Integer code;

	@JsonView(Views.Public.class)
	private String message;

	public RESTError() {
		super();
	}

	public RESTError(Integer code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

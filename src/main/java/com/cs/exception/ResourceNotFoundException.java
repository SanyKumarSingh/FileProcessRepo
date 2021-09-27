package com.cs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * To Handle Resource Not Found Exception from RESTful Services
 * 
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String message){
    	super(message);
    }
	
	public ResourceNotFoundException(String message, Throwable exception){
    	super(message, exception);
    }
}

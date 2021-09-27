package com.cs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * To Handle Invalid Request to RESTful Services
 * 
 */
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidRequestException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidRequestException(String message){
    	super(message);
    }
	
	public InvalidRequestException(String message, Throwable exception){
    	super(message, exception);
    }
}
package com.cs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * To Handle Database Exception after RESTful Service call
 * 
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends Exception {

	private static final long serialVersionUID = 1L;

	public InternalServerException(String message){
    	super(message);
    }
	
	public InternalServerException(String message, Throwable exception) {
    	super(message, exception);
    }
}
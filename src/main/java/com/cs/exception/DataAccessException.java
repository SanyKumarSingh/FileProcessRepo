package com.cs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * To Handle Database Exception after RESTful Service call
 * 
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DataAccessException extends Exception {

	private static final long serialVersionUID = 1L;

	public DataAccessException(String message){
    	super(message);
    }
	
	public DataAccessException(String message, Throwable exception) {
    	super(message, exception);
    }
}

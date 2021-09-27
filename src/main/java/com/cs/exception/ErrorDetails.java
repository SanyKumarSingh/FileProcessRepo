package com.cs.exception;

import java.util.Date;

/*
 * To Customize the Error Response Structure
 * {
 * "timestamp":"YYYY-MM-DD",
 * "message":"As thrown from the Controller in the User defined Exception",
 * "details":"uri=/some/wrong/uri/"
 * }
 * 
 */
public class ErrorDetails {
	private Date timestamp;
	private String message;
	private String details;

	public ErrorDetails(Date timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}
}

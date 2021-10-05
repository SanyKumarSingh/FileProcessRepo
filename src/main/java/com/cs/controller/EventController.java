package com.cs.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs.exception.DataAccessException;
import com.cs.exception.InvalidRequestException;
import com.cs.service.EventService;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class EventController {
	
	private static final Logger logger = LoggerFactory.getLogger(EventController.class);
	
	@Autowired
	private EventService eventService;
	
	@PostMapping("/fileProcessing")
	public ResponseEntity<Object> processFile(@Valid @RequestBody String filePath)
			throws DataAccessException, InvalidRequestException {
		logger.info("Received request to process a File");
		final int countLongEvents = eventService.processFile(filePath);
		return ResponseEntity.ok().body("Total count of long events that take longer than 4ms" + countLongEvents);
	}
	
	
	@GetMapping("/fileProcessing")
	public ResponseEntity<Object> processFile()
			throws DataAccessException, InvalidRequestException {
		logger.info("Received request to process a File");
		String filePath = "../ShoppingMartDemo/src/main/resources/logfile.txt";
		final int countLongEvents = eventService.processFile(filePath);
		return ResponseEntity.ok().body("Total count of long events that take longer than 4ms" + countLongEvents);
	}

}

package com.cs.service.impl;

import java.io.IOException;

import org.json.JSONException;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.exception.DataAccessException;
import com.cs.service.EventService;
import com.cs.utility.FileProcessor;

@Service
public class EventServiceImpl implements EventService {
	
	private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

	@Autowired
	private FileProcessor fileProcessor;

	@Override
	public int processFile(String filePath) throws DataAccessException {
		logger.info("Processing request to read event details in log file and save the event details");
		int countLongEvents = 0;
		try {
			countLongEvents = fileProcessor.readLargeFileInChunks(filePath);
		} catch (IOException | ParseException | JSONException e) {
			throw new DataAccessException("Event details could not be Processed");
		}
		
		return countLongEvents;
	}

}

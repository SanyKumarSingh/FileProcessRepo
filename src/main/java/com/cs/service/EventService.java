package com.cs.service;

import com.cs.exception.DataAccessException;

public interface EventService {
	
	public int processFile(String filePath) throws DataAccessException;

}


package com.cs.utility;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.dto.EventDTO;
import com.cs.model.Event;
import com.cs.repository.EventRepository;
import com.cs.service.impl.EventServiceImpl;

@Component
public class FileProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
	
	@Autowired
	private EventRepository eventRepository;
	
	private Map<String, EventDTO> events = new HashMap<String, EventDTO>();
	
	private int countLongEvents = 0;

	/*public static void main(String[] args) throws IOException, ParseException, JSONException {
		String filePath = "../ShoppingMartDemo/src/main/resources/logfile.txt";
		readLargeFileInChunks(filePath);
	}*/

	/*
	 * When we use a Java IO to read a file, the slowest part of the process is when
	 * the file contents are actually transferred between the hard disk and the JVM
	 * memory. Thus, to make File IO faster we can reduce the number of times the
	 * data transfer happens.
	 * 
	 * In memory transfer, is a fast way to read file, however holding entire
	 * content of a file in memory. For example byte[] or List<String> is not
	 * practical with large files and could lead to OutOfMemoryException.
	 * 
	 * Java Stream of strings, provided by BufferedReader to process a large file
	 * are lazy, so it performs better on the memory, but takes longer time to
	 * finish the file processing.
	 * 
	 * The optimal way, fastest and most memory efficient way of large file handling
	 * is processing large amount of data in Java IO using BufferedInputStream with
	 * a buffer size. Example buffer size (4 * 1024) which could be adjust as per
	 * file size.
	 */
	public int readLargeFileInChunks(String filePath) throws IOException, ParseException, JSONException {
		logger.info("Processing request to read event details in log file and save the event details");
		countLongEvents = 0;
		try (InputStream inputStream = new FileInputStream(filePath);
				BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);) {
			byte[] buffer = new byte[200];
			int read;
			String fileContentInChunk = "";
			String remainingTextFromChunk = "";

			// Read file content in chunk
			while ((read = bufferedInputStream.read(buffer, 0, buffer.length)) != -1) {
				fileContentInChunk = new String(buffer, 0, read);
				remainingTextFromChunk = processFileContent(remainingTextFromChunk + fileContentInChunk);
			}
		}
		
		return countLongEvents;
	}

	/*
	 * Format the chunk of file content to form proper JSON data. Split the JSON
	 * data chunk for line by line JSON processing and store the events in a Map.
	 */
	private String processFileContent(String fileContent) throws IOException, ParseException, JSONException {
		String json = fileContent.substring(0, fileContent.lastIndexOf("}") + 1);
		String remainingTextFromChunk = fileContent.substring(fileContent.lastIndexOf("}") + 1);

		// Split the JSON data chunk to pass each line of JSON for processing.
		for (String jsonData : json.split("((?<=}))")) {
			System.out.println(jsonData);
			EventDTO eventDTO = parseJSONLineByLine(jsonData);
			
			if (events.containsKey(eventDTO.getId())) {
				// Calculate the event duration and 
				System.out.println("Match Found");
				eventDTO = processEventDetails(events.get(eventDTO.getId()), eventDTO);
				
				// Clean up the Map after use to keep it light weight
				events.remove(eventDTO.getId());
				
				//Save the event details in database
				Event event = convertEventDTOtoModel(eventDTO);
				eventRepository.save(event);
			} else {
				events.put(eventDTO.getId(), eventDTO);
				System.out.println("Match Not Found");
			}
		}

		return remainingTextFromChunk;
	}

	/*
	 * Parse the JSON Line by Line to store the event details in EventDTO.
	 */
	private EventDTO parseJSONLineByLine(String json) throws ParseException, JSONException {
		EventDTO event = new EventDTO();

		JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;
		jsonObject = (JSONObject) parser.parse(json);

		String id = (String) jsonObject.get("id");
		System.out.println(id);
		event.setId(id);

		String state = (String) jsonObject.get("state");
		System.out.println(state);
		event.setState(state);

		Long timestamp = (Long) jsonObject.get("timestamp");
		System.out.println(timestamp);
		event.setTimestamp(timestamp);

		String type = (String) jsonObject.get("type");
		System.out.println(type);
		event.setType(type);

		String host = (String) jsonObject.get("host");
		System.out.println(host);
		event.setHost(host);

		return event;
	}

	/*
	 * After reading the event calculate the event duration.
	 * Set the Alert Flag based on Event Duration - true if the event took longer than 4ms, otherwise false
	 */
	private EventDTO processEventDetails(EventDTO storedEvent, EventDTO currentEvent) {
		currentEvent.setDuration(Math.abs(storedEvent.getTimestamp() - currentEvent.getTimestamp()));
		System.out.println(currentEvent.getDuration());
		
		if(currentEvent.getDuration() > 4) {
			currentEvent.setAlert(true);
			countLongEvents++;
		}
		System.out.println(currentEvent.isAlert());
		
		if(currentEvent.getState() != null && currentEvent.getState().equalsIgnoreCase("STARTED")) {
			currentEvent.setStartTimestamp(currentEvent.getTimestamp());
			currentEvent.setEndTimestamp(storedEvent.getTimestamp());
		} else {
			currentEvent.setEndTimestamp(currentEvent.getTimestamp());
			currentEvent.setStartTimestamp(storedEvent.getTimestamp());
		}
		System.out.println(currentEvent.getStartTimestamp());
		System.out.println(currentEvent.getEndTimestamp());
		
		return currentEvent;
	}
	

	/*
	 * Convert Event DTO to Event Data Model 
	 */
	private Event convertEventDTOtoModel(EventDTO eventDto) {
		Event event = new Event();
		event.setEventId(eventDto.getId());
		event.setEventDuration(eventDto.getDuration());
		event.setHost(eventDto.getHost());
		event.setType(eventDto.getType());
		event.setAlert(eventDto.isAlert() ? "Y" : "N");
		return event;
	}

	/*
	 * Parse the JSON in chunk to store the event details in EventDTO.
	 */
	private void parseJSONInChunk(String json) throws IOException, ParseException, JSONException {
		JSONArray array = new JSONArray(json);
		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);
			System.out.println(object.getString("id"));
			System.out.println(object.getString("state"));
			System.out.println(object.getLong("timestamp"));
		}
	}

}
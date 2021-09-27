
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

import com.cs.dto.EventDTO;

public class ProcessLargeFile {
	
	private static Map<String, EventDTO> events = new HashMap<String, EventDTO>();

	public static void main(String[] args) throws IOException, ParseException, JSONException {
		String filePath = "../ShoppingMartDemo/src/main/resources/logfile.txt";
		readLargeFileInChunks(filePath);
	}

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
	private static void readLargeFileInChunks(String filePath) throws IOException, ParseException, JSONException {
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
	}

	/*
	 * Format the chunk of file content to form proper JSON data. Split the JSON
	 * data chunk for line by line JSON processing and store the events in a Map.
	 */
	private static String processFileContent(String fileContent) throws IOException, ParseException, JSONException {
		

		String json = fileContent.substring(0, fileContent.lastIndexOf("}") + 1);
		String remainingTextFromChunk = fileContent.substring(fileContent.lastIndexOf("}") + 1);

		// Split the JSON data chunk to pass each line of JSON for processing.
		for (String jsonData : json.split("((?<=}))")) {
			System.out.println(jsonData);
			EventDTO event = parseJSONLineByLine(jsonData);
			
			if (events.containsKey(event.getId())) {
				// Calculate the event duration and 
				System.out.println("Match Found");
				event = processEventDetails(events.get(event.getId()), event);
				
				// Clean up the Map after use to keep it light weight
				events.remove(event.getId());
				
				//Save the event details in database
				//......
			} else {
				events.put(event.getId(), event);
				System.out.println("Match Not Found");
			}
		}

		return remainingTextFromChunk;
	}

	/*
	 * Parse the JSON Line to store the event details in EventDTO.
	 */
	private static EventDTO parseJSONLineByLine(String json) throws ParseException, JSONException {
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
		event.setState(type);

		String host = (String) jsonObject.get("host");
		System.out.println(host);
		event.setState(host);

		return event;
	}

	/*
	 * Parse the JSON Line to store the event details in EventDTO.
	 */
	private static EventDTO processEventDetails(EventDTO storedEvent, EventDTO currentEvent) {
		currentEvent.setDuration(Math.abs(storedEvent.getTimestamp() - currentEvent.getTimestamp()));
		System.out.println(currentEvent.getDuration());
		
		if(currentEvent.getDuration() > 4) {
			currentEvent.setAlert(true);
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

	
	private static void parseJSONInChunk(String json) throws IOException, ParseException, JSONException {
		JSONArray array = new JSONArray(json);
		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);
			System.out.println(object.getString("id"));
			System.out.println(object.getString("state"));
			System.out.println(object.getLong("timestamp"));
		}
	}

}
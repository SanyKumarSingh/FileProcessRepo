package com.cs.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "events")
public class Event implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 186576564667L;
	
	@Id
	private String eventId;
	private Long eventDuration;
	private String type;
	private String host;
	private String alert;
	
	public Event() {

	}

	public Event(String eventId, String type, String host, String alert, Long eventDuration) {
		this.eventId = eventId;
		this.eventDuration = eventDuration;
		this.type = type;
		this.host = host;
		this.alert = alert;
	}
	
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public Long getEventDuration() {
		return eventDuration;
	}
	public void setEventDuration(Long eventDuration) {
		this.eventDuration = eventDuration;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getAlert() {
		return alert;
	}
	public void setAlert(String alert) {
		this.alert = alert;
	}
	
}

package com.cs.dto;

import java.io.Serializable;

public class EventDTO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1867744564887L;

	public EventDTO() {

	}

	private String id;
	private String state;
	private Long timestamp;
	private String type;
	private String host;
	private Long startTimestamp;
	private Long endTimestamp;
	private Long duration;
	private boolean alert;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Long getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(Long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public Long getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(Long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public boolean isAlert() {
		return alert;
	}

	public void setAlert(boolean alert) {
		this.alert = alert;
	}

	@Override
	public String toString() {
		return "Event Details [Id=" + id + ", state=" + state + ", Timestamp=" + timestamp + ", Type=" + type
				+ ", Host=" + host + "]";
	}
}


package com.smartparenting.beans;

import java.io.Serializable;

public class CallBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String id = "";
	String phoneNumber = "";
	String type = "";
	long startTime;
	long endTime;
	String duration = "0:0:0";
	String callStart = "";
	String callStop = "";
	String date = "";
	String time = "";
	String setAudio = "";

	public String getSetAudio() {
		return setAudio;
	}

	public void setSetAudio(String setAudio) {
		this.setAudio = setAudio;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCallStart() {
		return callStart;
	}

	public void setCallStart(String callStart) {
		this.callStart = callStart;
	}

	public String getCallStop() {
		return callStop;
	}

	public void setCallStop(String callStop) {
		this.callStop = callStop;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}

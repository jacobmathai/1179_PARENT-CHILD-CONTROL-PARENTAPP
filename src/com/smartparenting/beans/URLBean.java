package com.smartparenting.beans;

import java.io.Serializable;

public class URLBean implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String title = "";
	String url = "";
	String time = "";
	String date = "";
	String visits = "";

	public URLBean(String title, String url, String time, String date,
			String visits) {
		super();
		this.title = title;
		this.url = url;
		this.time = time;
		this.date = date;
		this.visits = visits;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public String getTime() {
		return time;
	}

	public String getDate() {
		return date;
	}

	public String getVisits() {
		return visits;
	}
}

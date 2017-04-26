package com.kjbessinger.lametime.model;

public class OutputMessage {
	private User user;
	private String text;
	private String time;
	
	public OutputMessage(User user, String text, String time) {
		this.user = user;
		this.text = text;
		this.time = time;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
}

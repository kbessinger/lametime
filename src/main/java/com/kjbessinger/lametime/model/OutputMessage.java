package com.kjbessinger.lametime.model;

import java.util.UUID;

public class OutputMessage {
	private String id;
	private MiniUser user;
	private String text;
	private String time;
	
	public OutputMessage(MiniUser user, String text, String time) {
		this.id = UUID.randomUUID().toString();
		this.user = user;
		this.text = text;
		this.time = time;
	}
	
	public MiniUser getUser() {
		return user;
	}

	public void setUser(MiniUser user) {
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}

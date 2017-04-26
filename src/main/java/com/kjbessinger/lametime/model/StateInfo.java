package com.kjbessinger.lametime.model;

public class StateInfo {
	private ConnectionInfo connectionInfo;
	private String state;
	private User user;
	
	public StateInfo(ConnectionInfo connectionInfo, String state, User user) {
		this.connectionInfo = connectionInfo;
		this.state = state;
		this.user = user;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public ConnectionInfo getConnectionInfo() {
		return connectionInfo;
	}

	public void setConnectionInfo(ConnectionInfo connectionInfo) {
		this.connectionInfo = connectionInfo;
	}
	
	
}

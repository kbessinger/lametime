package com.kjbessinger.lametime.model;

public class StateInfo {
	private ConnectionInfo connectionInfo;
	private String state;
	private MiniUser user;

	public StateInfo(ConnectionInfo connectionInfo, String state, User user) {
		this.connectionInfo = connectionInfo;
		this.state = state;
		this.user = new MiniUser(user);
	}

	public StateInfo(ConnectionInfo connectionInfo, String state, MiniUser user) {
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
	public MiniUser getUser() {
		return user;
	}
	public void setUser(MiniUser user) {
		this.user = user;
	}

	public ConnectionInfo getConnectionInfo() {
		return connectionInfo;
	}

	public void setConnectionInfo(ConnectionInfo connectionInfo) {
		this.connectionInfo = connectionInfo;
	}
	
	
}

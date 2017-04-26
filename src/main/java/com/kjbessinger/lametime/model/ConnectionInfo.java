package com.kjbessinger.lametime.model;

import java.util.Collection;

public class ConnectionInfo {
	private VersionInfo versionInfo;
	private Collection<User> users;
	
	public ConnectionInfo(VersionInfo versionInfo, CurrentInfo currentInfo) {
		this.versionInfo = versionInfo;
		this.users = currentInfo.getUsers();
	}
	
	public VersionInfo getVersionInfo() {
		return versionInfo;
	}
	public void setVersionInfo(VersionInfo versionInfo) {
		this.versionInfo = versionInfo;
	}
	public Collection<User> getUsers() {
		return users;
	}
	public void setUsers(Collection<User> users) {
		this.users = users;
	}
	
	
}

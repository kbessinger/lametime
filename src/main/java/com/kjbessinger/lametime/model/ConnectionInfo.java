package com.kjbessinger.lametime.model;

import java.util.Collection;

public class ConnectionInfo {
	private VersionInfo versionInfo;
	private Collection<MiniUser> users;
	
	public ConnectionInfo(VersionInfo versionInfo, CurrentInfo currentInfo) {
		this.versionInfo = versionInfo;
		this.users = currentInfo.getMiniUsers();
	}
	
	public VersionInfo getVersionInfo() {
		return versionInfo;
	}
	public void setVersionInfo(VersionInfo versionInfo) {
		this.versionInfo = versionInfo;
	}
	public Collection<MiniUser> getUsers() {
		return users;
	}
	public void setUsers(Collection<MiniUser> users) {
		this.users = users;
	}
	
	
}

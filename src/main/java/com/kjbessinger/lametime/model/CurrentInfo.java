package com.kjbessinger.lametime.model;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class CurrentInfo {
	private Map<String, User> users;
	
	public CurrentInfo() {
		users = new ConcurrentHashMap<String, User>();
	}

	public Map<String, User> getUsersMap() {
		return users;
	}
	
	public Collection<User> getUsers() {
		return users.values();
	}

	public void setUsersMap(Map<String, User> users) {
		this.users = users;
	}
	
	public void addUser(User user) {
		this.users.put(user.getSessionId(), user);
	}
	
	public void removeUser(String sessionId) {
		users.remove(sessionId);
	}
	
	public User findUserBySessionId(String sessionId) {
		return users.get(sessionId);
	}
	
	public User findUserByConnectionId(String connectionId) {
		for(User user : users.values()) {
			if(user.getCid().equalsIgnoreCase(connectionId)) {
				return user;
			}
		}
		return null;
	}
	
}

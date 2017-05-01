package com.kjbessinger.lametime.model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.springframework.stereotype.Component;

@Component
public class CurrentInfo {
	private Map<String, User> users;
	private Multimap<String, String> sessionsPerEmailMap;
	private Map<String, String> emailForSessionMap;
	
	public CurrentInfo() {
		users = new ConcurrentHashMap<String, User>();
		sessionsPerEmailMap = Multimaps.synchronizedMultimap(HashMultimap.<String, String>create());
		emailForSessionMap = Collections.synchronizedMap(new HashMap<String, String>());
	}

	public Map<String, User> getUsersMap() {
		return users;
	}
	
	public Collection<User> getUsers() {
		return users.values();
	}
	public Collection<MiniUser> getMiniUsers() {
		Collection<MiniUser> miniUsers = new ArrayList<MiniUser>();
		for(User user : users.values() ) {
			miniUsers.add(new MiniUser(user));
		}
		return miniUsers;
	}

	public void setUsersMap(Map<String, User> users) {
		this.users = users;
	}
	
	public void addUser(String sessionId, User user) {

		this.users.put(user.getEmail(), user);
		synchronized(sessionsPerEmailMap) {
			sessionsPerEmailMap.put(user.getEmail(), sessionId);
		}
		synchronized(emailForSessionMap) {
			emailForSessionMap.put(sessionId, user.getEmail());
		}

	}
	
	public void removeUser(String email, String sessionId) {
		synchronized(sessionsPerEmailMap) {
			sessionsPerEmailMap.remove(email, sessionId);
		}
		if(!sessionsPerEmailMap.containsKey(email)) {
			synchronized (emailForSessionMap) {
				emailForSessionMap.remove(sessionId);
				users.remove(email);
			}
		}
	}

	public User findUserByEmail(String email ) { return users.get(email); }
	public User findUserBySession(String sessionId) {
		return users.get(emailForSessionMap.get(sessionId));
	}
	
}

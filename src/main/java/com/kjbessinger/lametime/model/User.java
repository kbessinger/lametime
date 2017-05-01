package com.kjbessinger.lametime.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

public class User {
	private String sub;
	private String email;
	@JsonProperty("verified_email")
	private String verifiedEmail;
	private String name;
	@JsonProperty("given_name")
	private String givenName;
	@JsonProperty("family_name")
	private String familyName;
	private String link;
	private String picture;
	private String gender;
	private String locale;

	public User(Map<String, Object> map) {
		this.sub = map.containsKey("sub") ? (String) map.get("sub") : null;
		this.email = map.containsKey("email") ? (String) map.get("email") : null;
		this.verifiedEmail = map.containsKey("verified_email") ? (String) map.get("verified_email") : null;
		this.name = map.containsKey("name") ? (String) map.get("name") : null;
		this.givenName = map.containsKey("given_name") ? (String) map.get("given_name") : null;
		this.familyName = map.containsKey("family_name") ? (String) map.get("family_name") : null;
		this.link = map.containsKey("link") ? (String) map.get("link") : null;
		this.picture = map.containsKey("picture") ? (String) map.get("picture") : null;
		this.gender = map.containsKey("gender") ? (String) map.get("gender") : null;
		this.locale = map.containsKey("locale") ? (String) map.get("locale") : null;
	}

	public String getSub() {
		return sub;
	}

	public void setSub(String sub) {
		this.sub = sub;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVerifiedEmail() {
		return verifiedEmail;
	}

	public void setVerifiedEmail(String verifiedEmail) {
		this.verifiedEmail = verifiedEmail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
}

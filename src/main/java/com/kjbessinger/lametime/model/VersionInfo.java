package com.kjbessinger.lametime.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class VersionInfo {
	private String version;
	private String buildDate;
	
	public VersionInfo() {
      	Properties prop = new Properties();
    	InputStream is = null;
   		is = this.getClass().getResourceAsStream("/version.properties");
   		try {
			prop.load(is);
	   		this.setBuildDate(prop.getProperty("buildTimestamp"));
	   		this.setVersion(prop.getProperty("version"));					
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getBuildDate() {
		return buildDate;
	}
	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}
	
	
}

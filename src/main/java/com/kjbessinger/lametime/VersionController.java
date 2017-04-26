package com.kjbessinger.lametime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import com.kjbessinger.lametime.model.CurrentInfo;
import com.kjbessinger.lametime.model.User;


public class VersionController {
	
	@Autowired
	private CurrentInfo currentInfo;
	
	
	@Scheduled(fixedDelay = 10000L )
    public void publishVersionInfo() throws Exception {
    	/*Properties prop = new Properties();
    	InputStream is = null;
   		is = this.getClass().getResourceAsStream("/version.properties");
   		prop.load(is);    
   		VersionInfo vi = new VersionInfo();
   		vi.setBuildDate(prop.getProperty("buildTimestamp"));
   		vi.setVersion(prop.getProperty("version"));
   		template.convertAndSend("/topic/version", vi);
   		*/
		for(User user : currentInfo.getUsers()) {
			System.out.println("current users: " + user.getSessionId() + ":" + user.getCid() + ":" +  user.getNick());	
		}
		
    }
}

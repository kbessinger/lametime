package com.kjbessinger.lametime;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import com.kjbessinger.lametime.model.ConnectionInfo;
import com.kjbessinger.lametime.model.CurrentInfo;
import com.kjbessinger.lametime.model.StateInfo;
import com.kjbessinger.lametime.model.User;
import com.kjbessinger.lametime.model.VersionInfo;

@Component
public class StompConnectionEvent implements ApplicationListener<SessionConnectEvent> {
  
	@Autowired
	private SimpMessagingTemplate template;
	
	@Autowired
	private CurrentInfo currentInfo;
	
	@Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
 
        String  connectionId = sha.getNativeHeader("cid").get(0);
        String host = (String) sha.getSessionAttributes().get("host");
        String username = (String) sha.getSessionAttributes().get("username");
        Principal principal = (Principal) sha.getSessionAttributes().get("principal");
        User user = new User(sha.getSessionId(), connectionId);
        user.setHostName(host);
        user.setFullName(username);
        user.setPrincipal(principal);
        currentInfo.addUser(user);        
        template.convertAndSend("/topic/state",  new StateInfo(new ConnectionInfo(new VersionInfo(), currentInfo), "connected", user));
        //String time = new SimpleDateFormat("HH:mm").format(new Date());
        //template.convertAndSend("/topic/messages",  new OutputMessage("server", connectionId + " connected", time));
    }
}

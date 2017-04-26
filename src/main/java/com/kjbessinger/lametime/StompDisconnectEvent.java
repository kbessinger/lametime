package com.kjbessinger.lametime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.kjbessinger.lametime.model.ConnectionInfo;
import com.kjbessinger.lametime.model.CurrentInfo;
import com.kjbessinger.lametime.model.StateInfo;
import com.kjbessinger.lametime.model.User;
import com.kjbessinger.lametime.model.VersionInfo;

@Component
public class StompDisconnectEvent implements ApplicationListener<SessionDisconnectEvent> {
  
	@Autowired
	private SimpMessagingTemplate template;
	
	@Autowired
	private CurrentInfo currentInfo;
	
	@Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
 
        User user = currentInfo.findUserBySessionId(sha.getSessionId());
        if(user != null) {
        	System.out.println("disconnecting: " + user.getCid() + ":" + user.getSessionId() + ":" + user.getNick());
        	currentInfo.removeUser(sha.getSessionId());
            template.convertAndSend("/topic/state",  new StateInfo(new ConnectionInfo(new VersionInfo(), currentInfo), "connected", user));
        } else {
        	System.out.println("nobody to disconnect: " + sha.getSessionId());
        }
        
        //String time = new SimpleDateFormat("HH:mm").format(new Date());
        //template.convertAndSend("/topic/messages",  new OutputMessage("server", connectionId + " connected", time));
    }
}

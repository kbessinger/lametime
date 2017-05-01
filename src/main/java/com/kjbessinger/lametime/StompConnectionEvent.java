package com.kjbessinger.lametime;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import com.kjbessinger.lametime.model.ConnectionInfo;
import com.kjbessinger.lametime.model.CurrentInfo;
import com.kjbessinger.lametime.model.StateInfo;
import com.kjbessinger.lametime.model.User;
import com.kjbessinger.lametime.model.VersionInfo;

import javax.servlet.http.HttpSession;

@Component
public class StompConnectionEvent implements ApplicationListener<SessionConnectEvent> {
  
	@Autowired
	private SimpMessagingTemplate template;
	
	@Autowired
	private CurrentInfo currentInfo;

	@Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        //OAuth2Authentication oAuth2Authentication =
        //        (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        //Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        Principal principal = (Principal) sha.getSessionAttributes().get("principal");
        //User user  = (User) userAuthentication.getDetails();
        OAuth2Authentication auth = (OAuth2Authentication) principal;
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) auth.getUserAuthentication();
        User user = new User((Map<String, Object>)token.getDetails());
        //String  connectionId = sha.getNativeHeader("cid").get(0);
        //String host = (String) sha.getSessionAttributes().get("host");
        //String username = (String) sha.getSessionAttributes().get("username");
        //Principal principal = (Principal) sha.getSessionAttributes().get("principal");
        //User user = new User(sha.getSessionId(), connectionId);
        //user.setHostName(host);
        //user.setFullName(username);
        //user.setPrincipal(principal);
        currentInfo.addUser(sha.getSessionId(), user);
        template.convertAndSend("/topic/state",  new StateInfo(new ConnectionInfo(new VersionInfo(), currentInfo), "connected", user));
        //String time = new SimpleDateFormat("HH:mm").format(new Date());
        //template.convertAndSend("/topic/messages",  new OutputMessage("server", connectionId + " connected", time));
    }
}

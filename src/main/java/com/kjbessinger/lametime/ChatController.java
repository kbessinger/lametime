package com.kjbessinger.lametime;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.kjbessinger.lametime.model.ConnectionInfo;
import com.kjbessinger.lametime.model.CurrentInfo;
import com.kjbessinger.lametime.model.Message;
import com.kjbessinger.lametime.model.OutputMessage;
import com.kjbessinger.lametime.model.StateInfo;
import com.kjbessinger.lametime.model.StateRequest;
import com.kjbessinger.lametime.model.User;
import com.kjbessinger.lametime.model.VersionInfo;

@Controller
public class ChatController {

	@Autowired
	CurrentInfo currentInfo;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(SimpMessageHeaderAccessor sha, Message message) throws Exception {
    	User user = currentInfo.findUserBySessionId(sha.getSessionId());
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String esc = StringEscapeUtils.escapeJavaScript(message.getText());
        esc = StringEscapeUtils.escapeHtml(esc);
        return new OutputMessage(user, esc, time);
    }
    
    @MessageMapping("/state")
    @SendTo("/topic/state")
    public StateInfo getStateInfo(StateRequest stateRequest, Principal puser) throws Exception {        
        System.out.println("person logged in is: " + puser.getName());
    	User user = currentInfo.findUserByConnectionId(stateRequest.getConnectionId());    	
    	return new StateInfo(new ConnectionInfo(new VersionInfo(), currentInfo), "connected", user);    	
    }
}

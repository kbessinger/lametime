package com.kjbessinger.lametime;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.kjbessinger.lametime.model.*;
import emoji4j.EmojiUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

	@Autowired
	CurrentInfo currentInfo;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(SimpMessageHeaderAccessor sha, Message message) throws Exception {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        //String esc = StringEscapeUtils.escapeJavaScript(message.getText());
        //esc = StringEscapeUtils.escapeHtml(esc);
        return new OutputMessage(new MiniUser(currentInfo.findUserBySession(sha.getSessionId())), EmojiUtils.emojify(message.getText()), time);
    }
    
    @MessageMapping("/state")
    @SendTo("/topic/state")
    public StateInfo getStateInfo(SimpMessageHeaderAccessor sha, Principal puser) throws Exception {
    	return new StateInfo(new ConnectionInfo(new VersionInfo(), currentInfo), "connected", currentInfo.findUserBySession(sha.getSessionId()));
    }
}

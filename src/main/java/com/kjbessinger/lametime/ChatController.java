package com.kjbessinger.lametime;

import com.kjbessinger.lametime.com.kjbessinger.lametime.model.LametimeRequestMessage;
import com.kjbessinger.lametime.com.kjbessinger.lametime.model.LametimeResponseMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created by kjbes on 4/24/2017.
 */

@Controller
public class ChatController {

    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public LametimeResponseMessage sendMessage(LametimeRequestMessage request) {
        LametimeResponseMessage response = new LametimeResponseMessage();
        response.message = request.from + " said: " + request.message;
        return response;
    }
}

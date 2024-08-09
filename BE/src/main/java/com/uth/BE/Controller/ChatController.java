package com.uth.BE.Controller;

import com.uth.BE.Entity.ws.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;

//@CrossOrigin(origins = "http://localhost:5173")
@Controller
public class ChatController {

//    @MessageMapping("/chat.{userid}")
//    @SendToUser("/queue/private")
//    public String sendMessage(@DestinationVariable("userid") String userid, @Payload String message) {
//
//        return message + "by" + userid;
//    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessageNew(@Payload ChatMessage message) {
        message.setTimestamp(new Date());
        return message;
    }

}

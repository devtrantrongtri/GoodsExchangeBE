package com.uth.BE.Controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;


@Controller
public class ChatController {

    @MessageMapping("/chat.private.{userid}")
    @SendToUser("/queue/private")
    public String sendMessage(@DestinationVariable("userid") String userid, @Payload String message) {

        return message + "by" + userid;
    }

}

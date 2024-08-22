package com.uth.BE.Controller;

import com.uth.BE.Entity.ChatMessage;
import com.uth.BE.Service.ChatService;
import com.uth.BE.dto.ChatMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:5173")
@Controller
public class ChatController {

    private final ChatService chatService;
    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat.{userid}")
    @SendToUser("/queue/private")
    public String sendMessage(@DestinationVariable("userid") String userid, @Payload String message) {

        return message + "by" + userid;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessageNew(@Payload ChatMessage message) {
//        message.setTimestamp(new Date());
        return message;
    }

    // Endpoint để lấy danh sách tin nhắn giữa hai người dùng
    @GetMapping("chat/between/{senderId}/{recipientId}")
    @ResponseBody
    public List<ChatMessageDTO> getMessagesBetweenUsers(@PathVariable Integer senderId, @PathVariable Integer recipientId) {
        return chatService.getMessagesBetweenUsers(senderId, recipientId);
    }
}

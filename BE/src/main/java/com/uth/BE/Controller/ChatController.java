package com.uth.BE.Controller;

import com.uth.BE.Entity.ChatMessage;
import com.uth.BE.Entity.User;
import com.uth.BE.Service.ChatService;
import com.uth.BE.Service.UserService;
import com.uth.BE.dto.req.ChatMessageDTO;
import com.uth.BE.dto.req.ChatMessageReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:5173")
@Controller
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    @Autowired
    public ChatController(ChatService chatService,SimpMessagingTemplate messagingTemplate,UserService userService) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
    }

//    @MessageMapping("/chat.{userid}")
//    @SendToUser("/queue/private")
//    public String sendMessage(@DestinationVariable("userid") String userid, @Payload String message) {
//
//        return message + "by" + userid;
//    }
//
//    @MessageMapping("/chat")
//    @SendTo("/topic/messages")
//    public ChatMessage sendMessageNew(@Payload ChatMessage message) {
////        message.setTimestamp(new Date());
//        return message;
//    }
@MessageMapping("/chat.{userid}")
    @SendToUser("/queue/private")
    public String sendMessage(@DestinationVariable("userid") String userid, @Payload ChatMessageDTO message) {

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

/*
*      client : /app/sendMessage/{userid} to send
*               /user/{userid}/queue/private to receive message
*      server :
* */
//    @MessageMapping("/chatwith/{recipient}")
//    public void sendMessageToUser(@DestinationVariable String recipient, @Payload String message) {
//        // Gửi tin nhắn đến người nhận cụ thể qua điểm đến `/user/{recipient}/queue/private`
//        messagingTemplate.convertAndSend( "/queue/" + recipient, message);
//    }
    @MessageMapping("/chatwith/{recipient}")
//    @SendToUser
    public void sendMessageToUser(@DestinationVariable String recipient,
                                  @Payload ChatMessageReq message,
                                  Principal principal) {
        Integer senderId = Integer.parseInt(principal.getName());
        ChatMessage chatMessage = new ChatMessage();
        System.out.println(message);
        chatMessage.setMessageType(message.getMessageType());
        chatMessage.setContent(message.getContent());

        Optional<User> recipientEdit = userService.getUserById(message.getRecipient());
        Optional<User> senderEdit = userService.getUserById(senderId);

        if(recipientEdit.isPresent() && senderEdit.isPresent()) {
            chatMessage.setSender(senderEdit.get());
            chatMessage.setRecipient(recipientEdit.get());
        }
        // luu tin nhan vao database
        chatService.createMessage(chatMessage);
//        // Gửi tin nhắn đến người nhận cụ thể qua điểm đến `/user/{recipient}/queue/private`
//        messagingTemplate.convertAndSend( "/queue/" + recipient, message);
        // Send the message to the recipient
        messagingTemplate.convertAndSendToUser(
                String.valueOf(recipient), "/queue/private/chat", message);

//        // Optionally, send the message to the sender as well
//        messagingTemplate.convertAndSendToUser(
//                String.valueOf(senderId), "/queue/private/chat", message);
    }

    @MessageMapping("/sendToTopic/{channel}")
    public void sendMessageToTopic(@DestinationVariable String channel, @Payload String message) {
        // Gửi tin nhắn đến điểm đến /topic/{channel}
        messagingTemplate.convertAndSend("/topic/" + channel, message);
    }

}

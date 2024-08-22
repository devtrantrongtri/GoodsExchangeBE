package com.uth.BE.Service;

import com.uth.BE.Entity.ChatMessage;
import com.uth.BE.Entity.User;
import com.uth.BE.Repository.ChatMessageRepository;
import com.uth.BE.Service.Interface.IChatService;
import com.uth.BE.dto.ChatMessageDTO;
import com.uth.BE.dto.UserDTO;
import org.springframework.stereotype.Service;
import org.w3c.dom.Text;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

@Service
public class ChatService implements IChatService {
    private final ChatMessageRepository chatMessageRepository;
    public ChatService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    // Phương thức để lấy danh sách tin nhắn giữa hai người dùng
    public List<ChatMessageDTO> getMessagesBetweenUsers(Integer senderId, Integer recipientId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findMessagesBetweenUsers(senderId, recipientId);

        return chatMessages.stream().map(message -> {
            // Create sender UserDTO
//            UserDTO senderDTO = UserDTO.builder()
//                    .userId(message.getSender().getUserId())
//                    .username(message.getSender().getUsername())
//                    .email(message.getSender().getEmail())
//                    .phoneNumber(message.getSender().getPhoneNumber())
//                    .address(message.getSender().getAddress())
//                    .role(message.getSender().getRole())
//                    .createdAt(message.getSender().getCreatedAt())
//                    .build();
//
//            // Create recipient UserDTO
//            UserDTO recipientDTO = UserDTO.builder()
//                    .userId(message.getRecipient().getUserId())
//                    .username(message.getRecipient().getUsername())
//                    .email(message.getRecipient().getEmail())
//                    .phoneNumber(message.getRecipient().getPhoneNumber())
//                    .address(message.getRecipient().getAddress())
//                    .role(message.getRecipient().getRole())
//                    .createdAt(message.getRecipient().getCreatedAt())
//                    .build();
            User sender = message.getSender();
            User recipient = message.getRecipient();

            // Create ChatMessageDTO
            return ChatMessageDTO.builder()
                    .messageId(message.getMessageId())
                    .content(message.getContent())
                    .messageType(message.getMessageType())
                    .createAt(message.getCreateAt())
                    .sender(sender.getUserId())
                    .recipient(recipient.getUserId())
                    .build();
        }).collect(Collectors.toList());
    }



    public boolean createMessage(ChatMessage chatMessage) {
        try {
            // Lưu tin nhắn vào cơ sở dữ liệu
            chatMessageRepository.save(chatMessage);
            return true; // Trả về true nếu lưu thành công
        } catch (Exception e) {

            return false; // Trả về false nếu có lỗi xảy ra
        }
    }
}

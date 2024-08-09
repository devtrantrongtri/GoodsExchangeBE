package com.uth.BE.Service;

import com.uth.BE.Entity.ChatMessage;
import com.uth.BE.Entity.User;
import com.uth.BE.Repository.ChatMessageRepository;
import com.uth.BE.Service.Interface.IChatService;
import org.springframework.stereotype.Service;
import org.w3c.dom.Text;

import java.util.List;

@Service
public class ChatService implements IChatService {
    private final ChatMessageRepository chatMessageRepository;
    public ChatService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    // Phương thức để lấy danh sách tin nhắn giữa hai người dùng
    public List<ChatMessage> getMessagesBetweenUsers(Integer senderId, Integer recipientId) {
        return chatMessageRepository.findMessagesBetweenUsers(senderId, recipientId);
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

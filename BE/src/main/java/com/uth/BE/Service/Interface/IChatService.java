package com.uth.BE.Service.Interface;

import com.uth.BE.Entity.ChatMessage;
import com.uth.BE.dto.req.ChatMessageDTO;

import java.util.List;

public interface IChatService {
    boolean createMessage(ChatMessage chatMessage);
    // Phương thức để lấy danh sách tin nhắn giữa hai người dùng
    List<ChatMessageDTO> getMessagesBetweenUsers(Integer senderId, Integer recipientId);
}

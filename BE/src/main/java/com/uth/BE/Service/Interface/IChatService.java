package com.uth.BE.Service.Interface;

import com.uth.BE.Entity.ChatMessage;
import org.w3c.dom.Text;

import java.util.List;

public interface IChatService {
    boolean createMessage(ChatMessage chatMessage);
    // Phương thức để lấy danh sách tin nhắn giữa hai người dùng
    List<ChatMessage> getMessagesBetweenUsers(Integer senderId, Integer recipientId);
}

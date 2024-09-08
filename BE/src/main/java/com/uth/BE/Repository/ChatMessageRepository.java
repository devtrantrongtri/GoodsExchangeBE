package com.uth.BE.Repository;

import com.uth.BE.Entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    // Truy vấn để lấy danh sách tin nhắn giữa hai người dùng
    @Query("SELECT cm FROM ChatMessage cm WHERE (cm.sender.userId = :senderId AND cm.recipient.userId = :recipientId) " +
            "OR (cm.sender.userId = :recipientId AND cm.recipient.userId = :senderId) ORDER BY cm.createAt ASC")
    List<ChatMessage> findMessagesBetweenUsers(@Param("senderId") Integer senderId, @Param("recipientId") Integer recipientId);
}

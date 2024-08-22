package com.uth.BE.dto;

import com.uth.BE.Entity.model.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDTO {
    private Integer messageId;
    private Integer sender;
    private Integer recipient;
    private String content;
    private MessageType messageType;
    private LocalDateTime createAt;
}

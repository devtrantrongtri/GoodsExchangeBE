package com.uth.BE.dto.req;

import com.uth.BE.Entity.model.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageReq {
    private Integer sender;
    private Integer recipient;
    private String content;
    private MessageType messageType;
}

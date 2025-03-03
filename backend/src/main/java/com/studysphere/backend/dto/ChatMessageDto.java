package com.studysphere.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessageDto {
    private Long groupId;
    private Long senderId;
    private String message;
    private LocalDateTime timestamp;
}

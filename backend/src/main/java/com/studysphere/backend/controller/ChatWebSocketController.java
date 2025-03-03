package com.studysphere.backend.controller;

import com.studysphere.backend.dto.ChatMessageDto;
import com.studysphere.backend.model.ChatMessage;
import com.studysphere.backend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat/{groupId}")  // Receives messages for a specific group
    @SendTo("/topic/chat/{groupId}")    // Broadcasts to subscribed users
    public ChatMessage sendMessage(@DestinationVariable Long groupId, ChatMessageDto message) {
        // Save the chat message to the database
        ChatMessage savedMessage = chatService.saveMessage(
                message.getSenderId(),
                groupId,
                message.getMessage()
        );

        return savedMessage;
    }
}

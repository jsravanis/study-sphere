package com.studysphere.backend.controller;

import com.studysphere.backend.model.ChatMessage;
import com.studysphere.backend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/history/{groupId}")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@PathVariable Long groupId) {
        return ResponseEntity.ok(chatService.getMessagesForGroup(groupId));
    }
}

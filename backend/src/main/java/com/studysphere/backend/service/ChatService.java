package com.studysphere.backend.service;

import com.studysphere.backend.model.ChatMessage;
import com.studysphere.backend.model.StudyGroup;
import com.studysphere.backend.model.User;
import com.studysphere.backend.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private StudyGroupService studyGroupService;

    public ChatMessage saveMessage(Long userId, Long groupId, String messageContent) {
        User user = userService.getUserById(userId);
        StudyGroup studyGroup = studyGroupService.getStudyGroupById(groupId);

        ChatMessage message = new ChatMessage();
        message.setSender(user);
        message.setStudyGroup(studyGroup);
        message.setMessage(messageContent);
        message.setTimestamp(LocalDateTime.now());

        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getMessagesForGroup(Long groupId) {
        return chatMessageRepository.findByStudyGroupIdOrderByTimestamp(groupId);
    }
}

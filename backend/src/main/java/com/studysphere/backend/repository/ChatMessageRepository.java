package com.studysphere.backend.repository;

import com.studysphere.backend.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByStudyGroupIdOrderByTimestamp(Long studyGroupId);
}

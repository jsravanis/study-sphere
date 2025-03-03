package com.studysphere.backend.repository;

import com.studysphere.backend.model.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
    List<Flashcard> findByStudyGroupId(Long studyGroupId);
}

package com.studysphere.backend.service;

import com.studysphere.backend.model.Flashcard;
import com.studysphere.backend.model.StudyGroup;
import com.studysphere.backend.model.User;
import com.studysphere.backend.repository.FlashcardRepository;
import com.studysphere.backend.repository.StudyGroupRepository;
import com.studysphere.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlashcardService {

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private UserRepository userRepository;

    public Flashcard createFlashcard(Long studyGroupId, Long userId, String question, String answer) {
        StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> new RuntimeException("Study group not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Flashcard flashcard = new Flashcard();
        flashcard.setStudyGroup(studyGroup);
        flashcard.setCreatedBy(user);
        flashcard.setQuestion(question);
        flashcard.setAnswer(answer);

        return flashcardRepository.save(flashcard);
    }

    public List<Flashcard> getAllFlashcards(Long studyGroupId) {
        return flashcardRepository.findByStudyGroupId(studyGroupId);
    }
}

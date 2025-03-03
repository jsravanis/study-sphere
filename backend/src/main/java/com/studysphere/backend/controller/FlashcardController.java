package com.studysphere.backend.controller;

import com.studysphere.backend.dto.FlashcardRequest;
import com.studysphere.backend.model.Flashcard;
import com.studysphere.backend.service.FlashcardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flashcards")
public class FlashcardController {

    @Autowired
    private FlashcardService flashcardService;

    @PostMapping("/create")
    public ResponseEntity<Flashcard> createFlashcard(@RequestBody FlashcardRequest request) {
        Flashcard flashcard = flashcardService.createFlashcard(
                request.getStudyGroupId(),
                request.getUserId(),
                request.getQuestion(),
                request.getAnswer()
        );
        return ResponseEntity.ok(flashcard);
    }

    @GetMapping("/{studyGroupId}")
    public ResponseEntity<List<Flashcard>> getAllFlashcards(@PathVariable Long studyGroupId) {
        List<Flashcard> flashcards = flashcardService.getAllFlashcards(studyGroupId);
        return ResponseEntity.ok(flashcards);
    }
}

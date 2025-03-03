package com.studysphere.backend.dto;

import lombok.Data;

@Data
public class FlashcardRequest {
    private Long studyGroupId;
    private Long userId;
    private String question;
    private String answer;
}

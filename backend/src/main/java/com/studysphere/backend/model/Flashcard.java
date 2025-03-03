package com.studysphere.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("flashCardId")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "study_group_id", nullable = false)
    private StudyGroup studyGroup;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    private String question;
    private String answer;
}

package com.studysphere.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class StudyGroupPdf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "study_group_id", nullable = false)
    private StudyGroup studyGroup;

    @ManyToOne
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;

    private String fileName;
    private String fileType;
    private String filePath;
    private LocalDateTime uploadedAt;
}

package com.studysphere.backend.repository;

import com.studysphere.backend.model.StudyGroupPdf;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudyGroupPdfRepository extends JpaRepository<StudyGroupPdf, Long> {
    List<StudyGroupPdf> findByStudyGroupId(Long studyGroupId);
}

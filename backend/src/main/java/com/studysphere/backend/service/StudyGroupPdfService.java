package com.studysphere.backend.service;

import com.studysphere.backend.model.StudyGroup;
import com.studysphere.backend.model.StudyGroupPdf;
import com.studysphere.backend.model.User;
import com.studysphere.backend.repository.StudyGroupPdfRepository;
import com.studysphere.backend.repository.StudyGroupRepository;
import com.studysphere.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudyGroupPdfService {

    @Autowired
    private StudyGroupPdfRepository pdfRepository;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private UserRepository userRepository;

    public final String UPLOAD_DIR = "uploads/";

    public StudyGroupPdf uploadPdf(Long studyGroupId, Long userId, MultipartFile file) throws IOException {
        StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> new RuntimeException("Study group not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Save file to local storage
        String filePath = UPLOAD_DIR + file.getOriginalFilename();
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        Files.copy(file.getInputStream(), Paths.get(filePath));

        // Save metadata to database
        StudyGroupPdf pdf = new StudyGroupPdf();
        pdf.setStudyGroup(studyGroup);
        pdf.setUploadedBy(user);
        pdf.setFileName(file.getOriginalFilename());
        pdf.setFileType(file.getContentType());
        pdf.setFilePath(filePath);
        pdf.setUploadedAt(LocalDateTime.now());

        return pdfRepository.save(pdf);
    }

    public List<StudyGroupPdf> getAllPdfs(Long studyGroupId) {
        return pdfRepository.findByStudyGroupId(studyGroupId);
    }
}

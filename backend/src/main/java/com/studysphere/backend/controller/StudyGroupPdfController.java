package com.studysphere.backend.controller;

import com.studysphere.backend.dto.UploadPdfRequest;
import com.studysphere.backend.model.StudyGroupPdf;
import com.studysphere.backend.service.StudyGroupPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/pdfs")
public class StudyGroupPdfController {

    @Autowired
    private StudyGroupPdfService pdfService;

    @PostMapping("/upload")
    public ResponseEntity<StudyGroupPdf> uploadPdf(@ModelAttribute UploadPdfRequest request) throws IOException {
        return ResponseEntity.ok(pdfService.uploadPdf(request.getStudyGroupId(), request.getUserId(), request.getFile()));
    }

    @GetMapping("/studyGroup/{studyGroupId}")
    public ResponseEntity<List<StudyGroupPdf>> getAllPdfs(@PathVariable Long studyGroupId) {
        return ResponseEntity.ok(pdfService.getAllPdfs(studyGroupId));
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(pdfService.UPLOAD_DIR).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_PDF) // Ensure correct MIME type
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

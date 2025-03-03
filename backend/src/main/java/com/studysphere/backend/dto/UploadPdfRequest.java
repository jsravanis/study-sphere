package com.studysphere.backend.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UploadPdfRequest {
    private Long studyGroupId;
    private Long userId;
    private MultipartFile file;
}

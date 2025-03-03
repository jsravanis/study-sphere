package com.studysphere.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudyGroupWithRoleDTO {
    private Long studyGroupId;
    private String topic;
    private String description;
    private String role;
}

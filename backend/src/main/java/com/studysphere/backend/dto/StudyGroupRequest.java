package com.studysphere.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudyGroupRequest {
    private String topic;
    private String description;
    private Integer maxLimit;
    private Long createdUserId;
    private Boolean isPrivate;
    private List<String> invitedUserEmails;
}

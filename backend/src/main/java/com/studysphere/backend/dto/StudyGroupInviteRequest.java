package com.studysphere.backend.dto;

import lombok.Data;

@Data
public class StudyGroupInviteRequest {
    private Long studyGroupId;
    private String  invitedUserEmail;
    private Long createdUserId;
}

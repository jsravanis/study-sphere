package com.studysphere.backend.dto;

import com.studysphere.backend.model.StudyGroupInvitation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserProfileResponse {
    private Long userId;
    private String username;
    private String email;
    private List<StudyGroupWithRoleDTO> studyGroups;  // Study groups with roles
    private List<StudyGroupInvitation> invitations; // Invitations received
}

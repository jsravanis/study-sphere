package com.studysphere.backend.controller;

import com.studysphere.backend.dto.StudyGroupInviteRequest;
import com.studysphere.backend.dto.StudyGroupRequest;
import com.studysphere.backend.dto.StudyGroupWithUsersAndRoles;
import com.studysphere.backend.model.StudyGroup;
import com.studysphere.backend.model.UserRole;
import com.studysphere.backend.service.StudyGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/studygroups")
public class StudyGroupController {

    @Autowired
    private StudyGroupService studyGroupService;

    @PostMapping("/create")
    public ResponseEntity<StudyGroup> createStudyGroup(@RequestBody StudyGroupRequest request) {
        StudyGroup group = studyGroupService.createStudyGroup(request);
        return ResponseEntity.ok(group);
    }

    @PostMapping("/inviteUser")
    public ResponseEntity<Void> createStudyGroup(@RequestBody StudyGroupInviteRequest studyGroupInviteRequest) {
        StudyGroup studyGroupById = studyGroupService.getStudyGroupById(studyGroupInviteRequest.getStudyGroupId());
        studyGroupService.inviteUser(studyGroupInviteRequest.getInvitedUserEmail(), studyGroupInviteRequest.getCreatedUserId(), studyGroupById);
        return ResponseEntity.ok().build();
    }

    // API to get a specific study group by ID
    @GetMapping("/{groupId}")
    public ResponseEntity<StudyGroupWithUsersAndRoles> getStudyGroup(@PathVariable Long groupId) {
        StudyGroupWithUsersAndRoles group = studyGroupService.getStudyGroupWithUsersAndRoles(groupId);  // Fetch the study group by ID
        if (group == null) {
            return ResponseEntity.notFound().build();  // Return 404 if the group is not found
        }
        return ResponseEntity.ok(group);  // Return the study group details
    }

    // API to get all public study groups
    @GetMapping("/public/{userId}")
    public ResponseEntity<List<StudyGroup>> getAllPublicStudyGroups(@PathVariable Long userId) {
        List<StudyGroup> publicStudyGroups = studyGroupService.getAllPublicStudyGroups(userId);
        return ResponseEntity.ok(publicStudyGroups);  // Return the list of public groups
    }

    // API to assign a role to a user in a study group
    @PostMapping("/{groupId}/addUserToGroup/{userId}")
    public ResponseEntity<UserRole> addUserToGroup(
            @PathVariable Long userId,
            @PathVariable Long groupId) {
        UserRole userRole = studyGroupService.addUserToGroup(userId, groupId);
        return ResponseEntity.ok(userRole);
    }

    // API to accept an invitation to a private group
    @PostMapping("/acceptInvitation/{invitationId}")
    public ResponseEntity<?> acceptInvitation(@PathVariable Long invitationId) {
        try {
            studyGroupService.acceptInvitation(invitationId);
            return ResponseEntity.ok().build();
        }catch (RuntimeException ex) {
            // Return a 400 Bad Request with an error message if something goes wrong
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errorMessage", ex.getMessage()));
        }
    }

    // API to decline an invitation to a private group
    @PostMapping("/declineInvitation/{invitationId}")
    public ResponseEntity<Void> declineInvitation(@PathVariable Long invitationId) {
        studyGroupService.declineInvitation(invitationId);
        return ResponseEntity.ok().build();
    }
}

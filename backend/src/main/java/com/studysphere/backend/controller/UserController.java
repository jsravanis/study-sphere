package com.studysphere.backend.controller;

import com.studysphere.backend.dto.StudyGroupWithRoleDTO;
import com.studysphere.backend.dto.UserProfileResponse;
import com.studysphere.backend.model.InvitationStatus;
import com.studysphere.backend.model.StudyGroupInvitation;
import com.studysphere.backend.model.User;
import com.studysphere.backend.model.UserRole;
import com.studysphere.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        List<UserRole> userRoles = user.getUserRoles();
        List<StudyGroupInvitation> invitations = user.getReceivedInvitations().stream()
                .filter(invitation -> invitation.getStatus().equals(InvitationStatus.PENDING)).collect(Collectors.toList());

        // Convert UserRole to StudyGroupWithRoleDTO
        List<StudyGroupWithRoleDTO> studyGroupsWithRoles = userRoles.stream()
                .map(role -> new StudyGroupWithRoleDTO(
                        role.getStudyGroup().getId(),
                        role.getStudyGroup().getTopic(),
                        role.getStudyGroup().getDescription(),
                        role.getRole().toString()
                ))
                .toList();

        UserProfileResponse profileResponse = new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                studyGroupsWithRoles,
                invitations
        );

        return ResponseEntity.ok(profileResponse);
    }

}

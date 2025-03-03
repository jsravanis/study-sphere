package com.studysphere.backend.service;

import com.studysphere.backend.dto.StudyGroupRequest;
import com.studysphere.backend.dto.StudyGroupWithUsersAndRoles;
import com.studysphere.backend.dto.UserWithRole;
import com.studysphere.backend.model.InvitationStatus;
import com.studysphere.backend.model.Role;
import com.studysphere.backend.model.StudyGroup;
import com.studysphere.backend.model.StudyGroupInvitation;
import com.studysphere.backend.model.User;
import com.studysphere.backend.model.UserRole;
import com.studysphere.backend.repository.StudyGroupInvitationRepository;
import com.studysphere.backend.repository.StudyGroupRepository;
import com.studysphere.backend.repository.UserRepository;
import com.studysphere.backend.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudyGroupService {

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private StudyGroupInvitationRepository studyGroupInvitationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // Method to get a study group by its ID
    public StudyGroup getStudyGroupById(Long groupId) {
        return studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Study group not found with id " + groupId));
    }

    // Method to get a study group by its ID, including user names and roles
    public StudyGroupWithUsersAndRoles getStudyGroupWithUsersAndRoles(Long groupId) {
        // Fetch the study group by ID
        StudyGroup studyGroup = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Study group not found with id " + groupId));

        // Fetch the users and their roles in the study group
        List<UserRole> userRoles = userRoleRepository.findByStudyGroupId(groupId);
        List<UserWithRole> usersWithRoles = userRoles.stream()
                .map(userRole -> new UserWithRole(userRole.getUser().getUsername(), userRole.getRole()))
                .collect(Collectors.toList());

        // Create and return a StudyGroupWithUsersAndRoles DTO (or a similar object)
        return new StudyGroupWithUsersAndRoles(studyGroup, usersWithRoles);
    }


    // Fetch all public study groups
    public List<StudyGroup> getAllPublicStudyGroups(Long userId) {
        return new ArrayList<>(studyGroupRepository.findPublicGroupsNotJoined(userId));
    }

    // Create a private study group and invite users
    public StudyGroup createStudyGroup(StudyGroupRequest studyGroupDTO) {
        StudyGroup studyGroup = new StudyGroup();
        studyGroup.setTopic(studyGroupDTO.getTopic());
        studyGroup.setDescription(studyGroupDTO.getDescription());
        studyGroup.setMaxLimit(studyGroupDTO.getMaxLimit());
        studyGroup.setIsPrivate(studyGroupDTO.getIsPrivate());

        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);
        addCreatorToStudyGroup(studyGroupDTO.getCreatedUserId(), savedStudyGroup);

        if(studyGroupDTO.getIsPrivate()){
            // Create invitations for selected users
            for (String userEmail : studyGroupDTO.getInvitedUserEmails()) {
                inviteUser(userEmail, studyGroupDTO.getCreatedUserId(), savedStudyGroup);
            }
        }

        return studyGroup;
    }

    public void inviteUser(String userEmail, Long createdUserId, StudyGroup savedStudyGroup) {
        User invitedUser = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        User createdUser = userRepository.findById(createdUserId).orElseThrow(() -> new RuntimeException("User not found"));
        StudyGroupInvitation invitation = new StudyGroupInvitation();
        invitation.setCreatedUser(createdUser);
        invitation.setInvitedUser(invitedUser);
        invitation.setStudyGroup(savedStudyGroup);
        invitation.setStatus(InvitationStatus.PENDING);
        studyGroupInvitationRepository.save(invitation);
    }

    // Accept an invitation to a private group
    public void acceptInvitation(Long invitationId) {
        StudyGroupInvitation invitation = studyGroupInvitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));

        StudyGroup studyGroup = invitation.getStudyGroup();

        // Check if the group has reached its max limit
        if (studyGroup.getCurrentUserCount() >= studyGroup.getMaxLimit()) {
            throw new RuntimeException("The study group is full. You cannot join.");
        }

        invitation.setStatus(InvitationStatus.ACCEPTED);
        studyGroupInvitationRepository.save(invitation);

        // Add the user to the study group as a member (create UserRole)
        UserRole userRole = new UserRole();
        userRole.setUser(invitation.getInvitedUser());
        userRole.setStudyGroup(invitation.getStudyGroup());
        userRole.setRole(Role.MEMBER);

        userRoleRepository.save(userRole);

        // Increment current user count
        studyGroup.setCurrentUserCount(studyGroup.getCurrentUserCount() + 1);
        studyGroupRepository.save(studyGroup);
    }

    // Decline an invitation
    public void declineInvitation(Long invitationId) {
        StudyGroupInvitation invitation = studyGroupInvitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));

        invitation.setStatus(InvitationStatus.DECLINED);
        studyGroupInvitationRepository.save(invitation);
    }

    private void addCreatorToStudyGroup(Long createdUserId, StudyGroup savedStudyGroup) {
        User user = userService.getUserById(createdUserId);
        userService.assignRoleToUser(user, savedStudyGroup, Role.ADMIN);
    }

    public UserRole addUserToGroup(Long userId, Long groupId) {
        // Fetch user and study group from DB
        User user = userService.getUserById(userId);
        StudyGroup studyGroup = getStudyGroupById(groupId);

        // Assign the role to the user
        return userService.assignRoleToUser(user, studyGroup, Role.MEMBER);
    }
}

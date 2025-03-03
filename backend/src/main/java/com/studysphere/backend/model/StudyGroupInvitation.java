package com.studysphere.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class StudyGroupInvitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("invitationId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "created_user_id")
    private User createdUser;  // created user

    @ManyToOne
    @JoinColumn(name = "invited_user_id")
    private User invitedUser;  // invited user

    @ManyToOne
    @JoinColumn(name = "study_group_id")
    private StudyGroup studyGroup;  // The study group being invited to

    @Enumerated(EnumType.STRING)
    private InvitationStatus status;  // Pending, Accepted, Declined
}

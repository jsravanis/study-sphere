package com.studysphere.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("userRoleId")  // Maps the 'id' field to 'userId' in the JSON response
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-roles")
    private User user;

    @ManyToOne
    @JoinColumn(name = "study_group_id")
    @JsonManagedReference("study-group-roles")
    private StudyGroup studyGroup;

    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN, MEMBER, MENTOR
}

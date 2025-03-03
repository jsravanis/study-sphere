package com.studysphere.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class StudyGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("studyGroupId")  // Maps the 'id' field to 'userId' in the JSON response
    private Long id;
    private String topic;
    private String description;
    private Integer maxLimit;
    private Integer currentUserCount = 0; // Track number of users in the group
    private Boolean isPrivate;
    // Relationship with UserRole
    @JsonIgnore
    @JsonBackReference("study-group-roles")
    @OneToMany(mappedBy = "studyGroup")
    private List<UserRole> userRoles;
    // Relationship with StudyGroupInvitations
    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "studyGroup")
    private List<StudyGroupInvitation> invitations;

}

package com.studysphere.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "user_entity")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("userId")  // Maps the 'id' field to 'userId' in the JSON response
    private Long id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    
    // Relationship with UserRole
    @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<UserRole> userRoles;

    @JsonIgnore
    @OneToMany(mappedBy = "createdUser")
    private List<StudyGroupInvitation> createdInvitations;

    @JsonIgnore
    @OneToMany(mappedBy = "invitedUser")
    private List<StudyGroupInvitation> receivedInvitations;

}

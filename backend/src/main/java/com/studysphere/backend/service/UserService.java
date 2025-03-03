package com.studysphere.backend.service;

import com.studysphere.backend.dto.SignupRequest;
import com.studysphere.backend.model.Role;
import com.studysphere.backend.model.StudyGroup;
import com.studysphere.backend.model.User;
import com.studysphere.backend.model.UserRole;
import com.studysphere.backend.repository.StudyGroupRepository;
import com.studysphere.backend.repository.UserRepository;
import com.studysphere.backend.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
    }

    // Method to assign a role to a user in a specific study group
    public UserRole assignRoleToUser(User user, StudyGroup savedStudyGroup, Role role) {
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setStudyGroup(savedStudyGroup);
        userRole.setRole(role);
        UserRole userRoleSaved = userRoleRepository.save(userRole);

        // Increment current user count
        savedStudyGroup.setCurrentUserCount(savedStudyGroup.getCurrentUserCount() + 1);
        studyGroupRepository.save(savedStudyGroup);

        return userRoleSaved;
    }

    public User authenticateUser(String email, String password) {
        // Find the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with provided email"));

        // Check if the provided password matches the stored password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Return the authenticated user
        return user;
    }

      public User registerUser(SignupRequest signupRequest) {
        // Check if the email or username is already registered
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered");
        }
        if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }

        // Create a new user
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword())); // Encode the password

        // Save the user to the database
        return userRepository.save(user);
    }

}

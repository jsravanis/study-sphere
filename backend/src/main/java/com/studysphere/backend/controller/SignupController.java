package com.studysphere.backend.controller;

import com.studysphere.backend.config.JwtUtil;
import com.studysphere.backend.dto.AuthResponse;
import com.studysphere.backend.dto.SignupRequest;
import com.studysphere.backend.model.User;
import com.studysphere.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class SignupController {
    @Autowired
    private UserService userService; // business logic for user authentication

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        try {
            // Call the UserService to register the user
            User registeredUser = userService.registerUser(signupRequest);

            // Generate JWT token for the newly registered user
            String token = JwtUtil.generateToken(registeredUser.getUsername());  // Generate token with the username

            // Return the registered user and token with HTTP status 201 (Created)
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AuthResponse(registeredUser.getId(), token));  // Return user and token

        } catch (RuntimeException ex) {
            // Return a 400 Bad Request with an error message if something goes wrong
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errorMessage", ex.getMessage()));
        }
    }
}

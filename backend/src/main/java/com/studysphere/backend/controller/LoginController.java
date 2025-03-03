package com.studysphere.backend.controller;

import com.studysphere.backend.config.JwtUtil;
import com.studysphere.backend.dto.LoginRequest;
import com.studysphere.backend.dto.AuthResponse;
import com.studysphere.backend.model.User;
import com.studysphere.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/auth")
public class LoginController {
    @Autowired
    private UserService userService; // business logic for user authentication

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate the user based on provided email and password
            User user = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
            String token = JwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok(new AuthResponse(user.getId(), token));
        } catch (RuntimeException ex) {
            // Return a 401 Unauthorized status with a simple error message
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("errorMessage", ex.getMessage()));
        }
    }

}

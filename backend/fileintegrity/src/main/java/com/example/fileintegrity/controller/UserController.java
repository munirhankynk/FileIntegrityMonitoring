package com.example.fileintegrity.controller;

import com.example.fileintegrity.dto.RegisterRequest;
import com.example.fileintegrity.model.GenericResponse;
import com.example.fileintegrity.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<GenericResponse<String>> register(@RequestBody RegisterRequest request) {
        try {
            userService.registerUser(request.getEmail(), request.getUsername(), request.getPassword());

            GenericResponse<String> response = new GenericResponse<>(200, null, "User registered successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage());

            GenericResponse<String> response = new GenericResponse<>(400, null, "Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}


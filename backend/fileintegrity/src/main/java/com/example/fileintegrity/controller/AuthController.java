package com.example.fileintegrity.controller;

import com.example.fileintegrity.dto.AuthRequest;
import com.example.fileintegrity.model.GenericResponse;
import com.example.fileintegrity.service.AuthService;
import com.example.fileintegrity.service.UserService;
import com.example.fileintegrity.util.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/authentication/")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final JwtService jwtService;
    @PostMapping("login")
    public ResponseEntity<GenericResponse<String >> generateToken(@RequestBody AuthRequest authRequest, HttpServletResponse httpServletResponse) {
        GenericResponse<String> authResponse = authService.authenticateAndSendToken(authRequest, httpServletResponse);
        return ResponseEntity.status(authResponse.getStatusCode()).body(authResponse);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        request.getSession().invalidate();
        return ResponseEntity.ok("Logout successful");
    }
}
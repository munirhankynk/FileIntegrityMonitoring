package com.example.fileintegrity.service;

import com.example.fileintegrity.dto.AuthRequest;
import com.example.fileintegrity.model.GenericResponse;
import com.example.fileintegrity.util.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public GenericResponse<String> authenticateAndSendToken(AuthRequest request, HttpServletResponse httpServletResponse) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            if (authentication.isAuthenticated()) {
                String jwt = jwtService.generateToken(request.getUsername());
                ResponseCookie cookie = jwtService.createJwtCookie(jwt, 3600, "/");
                httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
                return new GenericResponse<>(HttpStatus.OK.value(), "Authentication successful", null);
            } else {
                return new GenericResponse<>(HttpStatus.UNAUTHORIZED.value(), "Authentication failed", null);
            }
        } catch (Exception e) {
            log.error("Failed authentication for username: {}", request.getUsername(), e);
            throw new BadCredentialsException("Failed authentication with USERNAME:" + request.getUsername(), e);
        }
    }
}

package com.example.fileintegrity.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {
    String generateToken(String username);
    Boolean validateToken(String token, UserDetails userDetails);
    String extractUser(String token);

}

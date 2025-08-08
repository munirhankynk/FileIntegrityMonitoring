package com.example.fileintegrity.service;

import com.example.fileintegrity.model.User;
import com.example.fileintegrity.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }



    @Transactional

    public void registerUser(String email, String username, String password) {
        try {
            if (userRepository.findByUsername(username).isPresent()) {
                throw new RuntimeException("Username already exists");// burayı catchla yakala
            }

            User newUser = new User();
            newUser .setEmail(email);
            newUser.setUsername(username);
            newUser.setPassword(passwordEncoder.encode(password));

            userRepository.save(newUser);

    } catch(
    Exception e)

    {
        log.error(e.getMessage());
    }
    }
}





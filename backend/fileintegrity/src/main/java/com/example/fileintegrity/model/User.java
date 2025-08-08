package com.example.fileintegrity.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    private String username;
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

}

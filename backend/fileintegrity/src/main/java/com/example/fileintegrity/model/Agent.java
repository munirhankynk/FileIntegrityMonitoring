package com.example.fileintegrity.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "agents")
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID agentId;

    @Column(nullable = false, unique = true)
    private String agentName;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private LocalDateTime lastHeartbeat;

    @Column(nullable = false, unique = true)
    private String activationToken;
}

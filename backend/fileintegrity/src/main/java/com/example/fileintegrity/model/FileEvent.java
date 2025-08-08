package com.example.fileintegrity.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class FileEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    private String filePath;
    private String eventType;
    private LocalDateTime timestamp;
}


package com.example.fileintegrity.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AgentDto {
    private UUID id;
    private String name;
    private String status;
    private LocalDateTime lastHeartbeat;
}


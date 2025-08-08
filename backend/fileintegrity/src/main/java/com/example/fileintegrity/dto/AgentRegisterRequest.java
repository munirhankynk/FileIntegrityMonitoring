package com.example.fileintegrity.dto;

import lombok.Data;

import java.util.List;

@Data
public class AgentRegisterRequest {
    private String agentName;
    private String activationToken;
    private List<String> watchDirs;
}
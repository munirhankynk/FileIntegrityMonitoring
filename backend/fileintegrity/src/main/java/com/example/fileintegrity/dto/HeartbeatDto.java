package com.example.fileintegrity.dto;

import lombok.Data;

@Data
public class HeartbeatDto {
    private String agentId;
    private String status;
    private String version;
}

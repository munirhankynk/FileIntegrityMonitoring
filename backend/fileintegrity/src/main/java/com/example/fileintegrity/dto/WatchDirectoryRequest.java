package com.example.fileintegrity.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class WatchDirectoryRequest {
    private UUID agentId;
    private List<String> directories;
}

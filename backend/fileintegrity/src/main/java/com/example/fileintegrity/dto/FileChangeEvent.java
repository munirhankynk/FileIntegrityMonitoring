package com.example.fileintegrity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileChangeEvent {
    private UUID agentId;
    private String filePath;
    private String eventType;  // "created", "modified", "deleted"
}

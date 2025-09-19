package com.example.fileintegrity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class DirectoryDto {
    private UUID id;
    private UUID agentId;
    private String path;
}

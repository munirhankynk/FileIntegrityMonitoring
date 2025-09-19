package com.example.fileintegrity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class EventDto {
    private UUID id;
    private UUID agentId;
    private String filePath;
    private String eventType;
    private LocalDateTime timestamp;
}

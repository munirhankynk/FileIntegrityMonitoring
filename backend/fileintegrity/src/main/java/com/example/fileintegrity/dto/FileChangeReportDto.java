package com.example.fileintegrity.dto;

import lombok.Data;
import java.util.List;

@Data
public class FileChangeReportDto {
    private String agentId;
    private List<FileChangeEvent> changes;
}
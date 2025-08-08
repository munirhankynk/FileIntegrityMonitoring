package com.example.fileintegrity.controller;

import com.example.fileintegrity.dto.FileChangeEvent;
import com.example.fileintegrity.service.FileEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/agent")
@RequiredArgsConstructor
public class FileEventController {

    private final FileEventService fileEventService;


    @PostMapping("/event")
    public ResponseEntity<String> reportFileEvent(@RequestBody FileChangeEvent event) {
        try {
            fileEventService.saveFileEvent(event);
            return ResponseEntity.ok("Event received");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}

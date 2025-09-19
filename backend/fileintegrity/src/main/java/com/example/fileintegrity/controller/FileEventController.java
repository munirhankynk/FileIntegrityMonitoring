package com.example.fileintegrity.controller;

import com.example.fileintegrity.dto.EventDto;
import com.example.fileintegrity.dto.FileChangeEvent;
import com.example.fileintegrity.model.FileEvent;
import com.example.fileintegrity.service.FileEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> listEvents(
            @RequestParam(value = "agentId", required = false) UUID agentId,
            @RequestParam(value = "eventType", required = false) String eventType,
            @RequestParam(value = "from", required = false) String fromIso,
            @RequestParam(value = "to", required = false) String toIso
    ) {
        LocalDateTime from = parseDateTime(fromIso);
        LocalDateTime to   = parseDateTime(toIso);

        List<FileEvent> events = fileEventService.find(agentId, eventType, from, to);
        List<EventDto> payload = events.stream().map(e -> new EventDto(
                e.getId(),
                e.getAgent().getAgentId(),
                e.getFilePath(),
                e.getEventType(),
                e.getTimestamp()
        )).toList();

        return ResponseEntity.ok(payload);
    }

    private LocalDateTime parseDateTime(String iso) {
        if (iso == null || iso.isBlank()) return null;
        try { return LocalDateTime.parse(iso); } catch (Exception ignored) { return null; }
    }
}

package com.example.fileintegrity.controller;


import com.example.fileintegrity.dto.CreateAgentRequest;
import com.example.fileintegrity.model.Agent;
import com.example.fileintegrity.model.WatchedDirectory;
import com.example.fileintegrity.service.AgentService;
import com.example.fileintegrity.service.WatchedDirectoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/agent")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;
    private final WatchedDirectoryService watchedDirectoryService;

    // Agent CLI tarafında kullanılacak aktivasyon endpoint'i
    @PostMapping("/activate")
    public ResponseEntity<?> activateAgent(@RequestBody Map<String, String> request) {
        String token = request.get("activationToken");
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().body("Activation token is required.");
        }

        try {
            Agent registeredAgent = agentService.registerAgentWithToken(token);
            return ResponseEntity.ok(registeredAgent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // CLI her 5 saniyede bir buraya heartbeat atar
    @PostMapping("/heartbeat")
    public ResponseEntity<Map<String, Object>> updateHeartbeat(@RequestBody Map<String, String> request) {
        String agentIdStr = request.get("agentId");
        if (agentIdStr == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing agentId"));
        }

        UUID agentId = UUID.fromString(agentIdStr);
        agentService.updateHeartbeat(agentId);

        List<WatchedDirectory> directories = watchedDirectoryService.getDirectoriesByAgentId(agentId);
        List<String> paths = directories.stream().map(WatchedDirectory::getPath).toList();

        return ResponseEntity.ok(Map.of(
                "message", "Heartbeat received",
                "directories", paths
        ));
    }

    // Agent izlediği dizinleri backend'e bildirir
    @PostMapping("/directories")
    public ResponseEntity<?> addDirectories(@RequestBody Map<String, Object> body) {
        try {
            UUID agentId = UUID.fromString((String) body.get("agentId"));
            List<String> paths = (List<String>) body.get("paths");

            watchedDirectoryService.saveWatchedDirectories(agentId, paths);
            return ResponseEntity.ok("Directories saved");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid data: " + e.getMessage());
        }
    }


    @PostMapping("/create")
    public ResponseEntity<?> createAgent(@RequestBody CreateAgentRequest request) {
        String name = request.getAgentName();
        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body("agentName is required");
        }

        Agent agent = agentService.createAgentStub(name);
        return ResponseEntity.ok(Map.of(
                "agentId", agent.getAgentId(),
                "activationToken", agent.getActivationToken()
        ));
    }
    @DeleteMapping("/directories/{id}")
    public ResponseEntity<?> deleteWatchedDirectory(@PathVariable("id") UUID id) {
        try {
            watchedDirectoryService.deleteDirectory(id);
            return ResponseEntity.ok("Directory deleted.");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Directory not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Silme işlemi başarısız: " + e.getMessage());
        }
    }
}



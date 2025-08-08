package com.example.fileintegrity.service;

import com.example.fileintegrity.dto.FileChangeEvent;
import com.example.fileintegrity.model.Agent;
import com.example.fileintegrity.model.WatchedDirectory;
import com.example.fileintegrity.repository.AgentRepository;
import com.example.fileintegrity.repository.WatchedDirectoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WatchedDirectoryService {

    private final WatchedDirectoryRepository watchedDirectoryRepository;
    private final FileEventService fileEventService;
    private final AgentRepository agentRepository;

    public WatchedDirectory addDirectory(WatchedDirectory directory) {
        return watchedDirectoryRepository.save(directory);
    }

    public List<WatchedDirectory> getAllDirectories() {
        return watchedDirectoryRepository.findAll();
    }

    public void deleteDirectory(UUID id) {
        log.warn("[DELETE] Silme denemesi: {}", id);
        watchedDirectoryRepository.deleteById(id);
        boolean stillExists = watchedDirectoryRepository.existsById(id);
        log.warn("[DELETE] Silindi mi? {}", !stillExists);
    }

    public void handleFileChange(UUID agentId, String filePath, String eventType) {
        FileChangeEvent event = new FileChangeEvent(agentId, filePath, eventType);
        fileEventService.saveFileEvent(event);
    }

    // Agent ID ile tüm dizinleri getir
    public List<WatchedDirectory> getDirectoriesByAgentId(UUID agentId) {
        return watchedDirectoryRepository.findByAgent_AgentId(agentId);
    }

    // Agent için gelen dizinleri kaydet
    public void saveWatchedDirectories(UUID agentId, List<String> paths) {
        Agent agent = agentRepository.findByAgentId(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        for (String path : paths) {
            boolean exists = watchedDirectoryRepository.existsByAgentAndPath(agent, path);
            if (exists) {
                log.info("[SKIP] Zaten kayıtlı: {}", path);
                continue;
            }

            WatchedDirectory dir = new WatchedDirectory();
            dir.setAgent(agent);
            dir.setPath(path);
            watchedDirectoryRepository.save(dir);
            log.info("[SAVE] Yeni dizin eklendi: {}", path);
        }
    }
}



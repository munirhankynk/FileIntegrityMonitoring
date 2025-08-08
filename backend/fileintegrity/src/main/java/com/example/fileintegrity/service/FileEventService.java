package com.example.fileintegrity.service;

import com.example.fileintegrity.dto.FileChangeEvent;
import com.example.fileintegrity.model.Agent;
import com.example.fileintegrity.model.FileEvent;
import com.example.fileintegrity.repository.AgentRepository;
import com.example.fileintegrity.repository.FileEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileEventService {

    private final FileEventRepository fileEventRepository;
    private final AgentRepository agentRepository;

    public FileEventService(FileEventRepository fileEventRepository, AgentRepository agentRepository) {
        this.fileEventRepository = fileEventRepository;
        this.agentRepository = agentRepository;
    }

    public void saveFileEvent(FileChangeEvent event) {
        UUID agentId = event.getAgentId();
        String filePath = event.getFilePath();
        String eventType = event.getEventType();

        Agent agent = agentRepository.findByAgentId(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found with id: " + agentId));

        FileEvent fileEvent = new FileEvent();
        fileEvent.setAgent(agent);
        fileEvent.setFilePath(filePath);
        fileEvent.setEventType(eventType);
        fileEvent.setTimestamp(LocalDateTime.now());

        fileEventRepository.save(fileEvent);
    }
}




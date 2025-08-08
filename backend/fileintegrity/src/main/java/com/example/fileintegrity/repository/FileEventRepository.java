package com.example.fileintegrity.repository;

import com.example.fileintegrity.model.FileEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface FileEventRepository extends JpaRepository<FileEvent, Long> {
    List<FileEvent> findByAgent_AgentId(UUID agentId);
}

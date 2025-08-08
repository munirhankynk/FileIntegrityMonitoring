package com.example.fileintegrity.repository;

import com.example.fileintegrity.model.Agent;
import com.example.fileintegrity.model.WatchedDirectory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WatchedDirectoryRepository extends JpaRepository<WatchedDirectory, UUID> {
    List<WatchedDirectory> findByAgent_AgentId(UUID agentId);
    void deleteByAgent_AgentId(UUID agentId);
    boolean existsByAgentAndPath(Agent agent, String path);

}
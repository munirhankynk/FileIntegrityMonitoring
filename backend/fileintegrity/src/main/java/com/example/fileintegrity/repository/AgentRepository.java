package com.example.fileintegrity.repository;

import com.example.fileintegrity.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AgentRepository extends JpaRepository<Agent, UUID> {
    Optional<Agent> findByActivationToken(String activationToken);
    Optional<Agent> findByAgentId(UUID agentId);
    List<Agent> findByLastHeartbeatBefore(LocalDateTime time);
}


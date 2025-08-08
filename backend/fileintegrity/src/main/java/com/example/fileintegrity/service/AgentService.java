package com.example.fileintegrity.service;


import com.example.fileintegrity.model.Agent;
import com.example.fileintegrity.repository.AgentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AgentService {

    private final AgentRepository agentRepository;

    public AgentService(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    @Transactional
    public Agent createAgentStub(String agentName) {
        try {
            UUID agentId = UUID.randomUUID();
            String token = UUID.randomUUID().toString();

            Agent agent = Agent.builder().agentName(agentName).activationToken(token).lastHeartbeat(LocalDateTime.now()).build();

            Agent savedAgent = agentRepository.save(agent);
            return savedAgent;
        }catch (RuntimeException e) {
            return null;
        }
    }
    // CLI üzerinden gelen tokeni kontrol edip agentı aktifleştirir
    public Agent registerAgentWithToken(String token) {
        Agent agent = agentRepository.findByActivationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid activation token."));

        if (agent.isActive()) {
            throw new RuntimeException("Agent already activated.");
        }

        agent.setActive(true);
        agent.setLastHeartbeat(LocalDateTime.now());

        return agentRepository.save(agent);
    }

    public void deactivateInactiveAgents() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(10);
        List<Agent> inactiveAgents = agentRepository.findByLastHeartbeatBefore(threshold);
        inactiveAgents.forEach(agent -> {
            agent.setActive(false);
            agentRepository.save(agent);
        });
    }

    public void updateHeartbeat(UUID agentId) {
        agentRepository.findByAgentId(agentId).ifPresent(agent -> {
            agent.setLastHeartbeat(LocalDateTime.now());
            agent.setActive(true);
            agentRepository.save(agent);
        });
    }
}



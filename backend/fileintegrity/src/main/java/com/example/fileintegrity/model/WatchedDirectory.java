package com.example.fileintegrity.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(
        name = "watched_directory",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"agent_id", "path"})
        }
)
public class WatchedDirectory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @Column(nullable = false)
    private String path;
}

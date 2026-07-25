package com.ocupa.ocupa.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "analytics")
@Getter
@Setter
public class Analytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "artista_id")
    private Artista artista;

    private String page;
    private String eventType;
    private Integer value = 1;
    private LocalDateTime createdAt;

    @PrePersist
    public void pre(){ createdAt = LocalDateTime.now(); }
}

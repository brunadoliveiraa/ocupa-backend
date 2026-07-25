package com.ocupa.ocupa.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String requesterNome;
    private String requesterContato;

    @ManyToOne
    @JoinColumn(name = "provider_artista_id")
    private Artista provider;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String status; // PENDING, ACCEPTED, REJECTED, COMPLETED
    private LocalDateTime criadoEm;

    @PrePersist
    public void pre(){ criadoEm = LocalDateTime.now(); if(status==null) status = "PENDING"; }
}

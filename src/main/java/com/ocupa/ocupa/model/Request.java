package com.ocupa.ocupa.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
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

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getRequesterNome() { return requesterNome; }
    public void setRequesterNome(String requesterNome) { this.requesterNome = requesterNome; }
    public String getRequesterContato() { return requesterContato; }
    public void setRequesterContato(String requesterContato) { this.requesterContato = requesterContato; }
    public Artista getProvider() { return provider; }
    public void setProvider(Artista provider) { this.provider = provider; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}

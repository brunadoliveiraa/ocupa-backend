package com.ocupa.ocupa.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "analytics")
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

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Artista getArtista() { return artista; }
    public void setArtista(Artista artista) { this.artista = artista; }
    public String getPage() { return page; }
    public void setPage(String page) { this.page = page; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public Integer getValue() { return value; }
    public void setValue(Integer value) { this.value = value; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

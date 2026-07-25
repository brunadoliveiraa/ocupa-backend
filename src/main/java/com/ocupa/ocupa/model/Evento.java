package com.ocupa.ocupa.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "evento")
@Getter
@Setter
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String local;
    private LocalDate dataEvento;
    private LocalTime horaEvento;
    private Integer publicoEstimado;

    @ManyToOne
    @JoinColumn(name = "artista_id")
    private Artista artista;

    @ManyToOne
    @JoinColumn(name = "espaco_id")
    private Espaco espaco;

    private Double latitude;
    private Double longitude;

    private String criadoPorEmail;

    @Column(columnDefinition = "LONGTEXT")
    private String fotoUrl;
}

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

    @Column(nullable = false)
    private String status = "PENDENTE";

    @Column(columnDefinition = "TEXT")
    private String motivoRejeicao;

    @Column(unique = true, nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private LocalDate dataEvento;
    private LocalTime horaEvento;

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

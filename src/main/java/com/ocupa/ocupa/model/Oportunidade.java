package com.ocupa.ocupa.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "oportunidade")
@Getter
@Setter
public class Oportunidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String status = "PENDENTE";

    @Column(columnDefinition = "TEXT")
    private String motivoRejeicao;

    @Column(unique = true, nullable = false)
    private String titulo;
    private String tipo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String local;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String inscricaoLink;
    private String criadoPorEmail;

    @Column(columnDefinition = "LONGTEXT")
    private String fotoUrl;
}

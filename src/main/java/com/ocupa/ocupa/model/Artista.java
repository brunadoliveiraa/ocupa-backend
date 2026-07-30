package com.ocupa.ocupa.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "artista")
@Getter
@Setter
public class Artista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String status = "PENDENTE";

    @Column(columnDefinition = "TEXT")
    private String motivoRejeicao;

    private String nome;
    private String categoria;

    @Column(columnDefinition = "TEXT")
    private String biografia;

    private String contato;
    private String cidade;
    private String redesSociais;
    @Column(columnDefinition = "LONGTEXT")
    private String fotoUrl;
    private Double latitude;
    private Double longitude;
}

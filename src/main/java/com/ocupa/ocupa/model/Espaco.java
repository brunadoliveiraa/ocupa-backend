package com.ocupa.ocupa.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "espaco")
@Getter
@Setter
public class Espaco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String endereco;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private Boolean cobertura = false;
    private Boolean iluminacao = false;
    private Boolean energia = false;
    private Boolean banheiro = false;
    private Integer capacidade;
    private Boolean permiteGrafite = false;
    private Boolean permiteBatalha = false;
    private Boolean permiteDanca = false;
    private Double latitude;
    private Double longitude;

    private String criadoPorEmail;

    @OneToMany(mappedBy = "espaco", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EspacoMedia> mediaItems = new ArrayList<>();
}

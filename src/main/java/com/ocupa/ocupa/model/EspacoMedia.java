package com.ocupa.ocupa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "espaco_media")
@Getter
@Setter
public class EspacoMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "espaco_id")
    @JsonIgnore
    private Espaco espaco;

    private String mediaType;

    @Column(length = 10485760)
    private String url;

    private String caption;
}

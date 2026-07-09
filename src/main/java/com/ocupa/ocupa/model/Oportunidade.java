package com.ocupa.ocupa.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "oportunidade")
public class Oportunidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titulo;
    private String tipo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String local;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String inscricaoLink;
    private String contato;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getTipo() { return tipo; }
    private String criadoPorEmail;

    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
    public String getInscricaoLink() { return inscricaoLink; }
    public void setInscricaoLink(String inscricaoLink) { this.inscricaoLink = inscricaoLink; }
    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }
    public String getCriadoPorEmail() { return criadoPorEmail; }
    public void setCriadoPorEmail(String criadoPorEmail) { this.criadoPorEmail = criadoPorEmail; }
}

package com.ocupa.ocupa.model;

import javax.persistence.*;

@Entity
@Table(name = "espaco")
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

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Boolean getCobertura() { return cobertura; }
    public void setCobertura(Boolean cobertura) { this.cobertura = cobertura; }
    public Boolean getIluminacao() { return iluminacao; }
    public void setIluminacao(Boolean iluminacao) { this.iluminacao = iluminacao; }
    public Boolean getEnergia() { return energia; }
    public void setEnergia(Boolean energia) { this.energia = energia; }
    public Boolean getBanheiro() { return banheiro; }
    public void setBanheiro(Boolean banheiro) { this.banheiro = banheiro; }
    public Integer getCapacidade() { return capacidade; }
    public void setCapacidade(Integer capacidade) { this.capacidade = capacidade; }
    public Boolean getPermiteGrafite() { return permiteGrafite; }
    public void setPermiteGrafite(Boolean permiteGrafite) { this.permiteGrafite = permiteGrafite; }
    public Boolean getPermiteBatalha() { return permiteBatalha; }
    public void setPermiteBatalha(Boolean permiteBatalha) { this.permiteBatalha = permiteBatalha; }
    public Boolean getPermiteDanca() { return permiteDanca; }
    public void setPermiteDanca(Boolean permiteDanca) { this.permiteDanca = permiteDanca; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getCriadoPorEmail() { return criadoPorEmail; }
    public void setCriadoPorEmail(String criadoPorEmail) { this.criadoPorEmail = criadoPorEmail; }
}

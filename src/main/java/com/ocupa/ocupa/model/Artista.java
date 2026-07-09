package com.ocupa.ocupa.model;

import javax.persistence.*;

@Entity
@Table(name = "artista")
public class Artista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String categoria;

    @Column(columnDefinition = "TEXT")
    private String biografia;

    private String contato;
    private String cidade;
    private String redesSociais;
    private String fotoUrl;
    private Double latitude;
    private Double longitude;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getRedesSociais() { return redesSociais; }
    public void setRedesSociais(String redesSociais) { this.redesSociais = redesSociais; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}

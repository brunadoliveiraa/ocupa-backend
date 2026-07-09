package com.ocupa.ocupa.model;

import javax.persistence.*;

@Entity
@Table(name = "portfolio")
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "artista_id")
    private Artista artista;

    private String headline;

    @Column(columnDefinition = "TEXT")
    private String about;

    @Column(columnDefinition = "TEXT")
    private String contacts; // store JSON string optionally

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Artista getArtista() { return artista; }
    public void setArtista(Artista artista) { this.artista = artista; }
    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }
    public String getAbout() { return about; }
    public void setAbout(String about) { this.about = about; }
    public String getContacts() { return contacts; }
    public void setContacts(String contacts) { this.contacts = contacts; }
}

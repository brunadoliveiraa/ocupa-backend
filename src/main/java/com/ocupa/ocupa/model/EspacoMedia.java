package com.ocupa.ocupa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

@Entity
@Table(name = "espaco_media")
public class EspacoMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "espaco_id")
    @JsonIgnore
    private Espaco espaco;

    private String mediaType;

    @Column(columnDefinition = "LONGTEXT")
    private String url;

    private String caption;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Espaco getEspaco() { return espaco; }
    public void setEspaco(Espaco espaco) { this.espaco = espaco; }
    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
}

package com.ocupa.ocupa.service;

import com.ocupa.ocupa.model.Artista;
import java.util.List;
import java.util.Optional;

public interface ArtistaService {
    List<Artista> findAll();
    Optional<Artista> findById(Integer id);
    Artista save(Artista artista);
    void delete(Artista artista);
}

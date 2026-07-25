package com.ocupa.ocupa.service;

import com.ocupa.ocupa.model.Artista;
import com.ocupa.ocupa.repository.ArtistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArtistaService {
    private final ArtistaRepository repository;

    public List<Artista> findAll() {
        return repository.findAll();
    }

    public Optional<Artista> findById(Integer id) {
        return repository.findById(id);
    }

    public Artista save(Artista artista) {
        return repository.save(artista);
    }

    public void delete(Artista artista) {
        repository.delete(artista);
    }
}

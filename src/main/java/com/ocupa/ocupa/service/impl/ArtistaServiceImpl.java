package com.ocupa.ocupa.service.impl;

import com.ocupa.ocupa.model.Artista;
import com.ocupa.ocupa.repository.ArtistaRepository;
import com.ocupa.ocupa.service.ArtistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArtistaServiceImpl implements ArtistaService {
    private final ArtistaRepository repository;

    @Override
    public List<Artista> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Artista> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Artista save(Artista artista) {
        return repository.save(artista);
    }

    @Override
    public void delete(Artista artista) {
        repository.delete(artista);
    }

    @Override
    public List<Artista> findByStatus(String status) {
        return repository.findByStatus(status);
    }
}

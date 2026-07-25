package com.ocupa.ocupa.service;

import com.ocupa.ocupa.model.Espaco;
import com.ocupa.ocupa.repository.EspacoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EspacoService {
    private final EspacoRepository repository;

    public List<Espaco> findAll() {
        return repository.findAll();
    }

    public Optional<Espaco> findById(Integer id) {
        return repository.findById(id);
    }

    public Espaco save(Espaco espaco) {
        return repository.save(espaco);
    }

    public void delete(Espaco espaco) {
        repository.delete(espaco);
    }
}

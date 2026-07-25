package com.ocupa.ocupa.service.impl;

import com.ocupa.ocupa.model.Espaco;
import com.ocupa.ocupa.repository.EspacoRepository;
import com.ocupa.ocupa.service.EspacoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EspacoServiceImpl implements EspacoService {
    private final EspacoRepository repository;

    @Override
    public List<Espaco> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Espaco> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Espaco save(Espaco espaco) {
        return repository.save(espaco);
    }

    @Override
    public void delete(Espaco espaco) {
        repository.delete(espaco);
    }
}

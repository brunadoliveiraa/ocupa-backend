package com.ocupa.ocupa.service;

import com.ocupa.ocupa.model.Oportunidade;
import com.ocupa.ocupa.repository.OportunidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OportunidadeService {
    private final OportunidadeRepository repository;

    public List<Oportunidade> findAll() {
        return repository.findAll();
    }

    public Optional<Oportunidade> findById(Integer id) {
        return repository.findById(id);
    }

    public Oportunidade save(Oportunidade oportunidade) {
        return repository.save(oportunidade);
    }

    public void delete(Oportunidade oportunidade) {
        repository.delete(oportunidade);
    }
}

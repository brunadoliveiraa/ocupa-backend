package com.ocupa.ocupa.service.impl;

import com.ocupa.ocupa.model.Oportunidade;
import com.ocupa.ocupa.repository.OportunidadeRepository;
import com.ocupa.ocupa.service.OportunidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OportunidadeServiceImpl implements OportunidadeService {
    private final OportunidadeRepository repository;

    @Override
    public List<Oportunidade> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Oportunidade> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Oportunidade save(Oportunidade oportunidade) {
        return repository.save(oportunidade);
    }

    @Override
    public void delete(Oportunidade oportunidade) {
        repository.delete(oportunidade);
    }

    @Override
    public List<Oportunidade> findByStatus(String status) {
        return repository.findByStatus(status);
    }

    @Override
    public List<Oportunidade> findByCriadoPorEmail(String email) {
        return repository.findByCriadoPorEmail(email);
    }
}

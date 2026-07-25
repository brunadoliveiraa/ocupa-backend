package com.ocupa.ocupa.service;

import com.ocupa.ocupa.model.Portfolio;
import com.ocupa.ocupa.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PortfolioService {
    private final PortfolioRepository repository;

    public List<Portfolio> findAll() {
        return repository.findAll();
    }

    public Optional<Portfolio> findById(Integer id) {
        return repository.findById(id);
    }

    public Portfolio save(Portfolio portfolio) {
        return repository.save(portfolio);
    }

    public void delete(Portfolio portfolio) {
        repository.delete(portfolio);
    }

    public Optional<Portfolio> findByArtistaId(Integer artistaId) {
        return repository.findByArtistaId(artistaId);
    }
}

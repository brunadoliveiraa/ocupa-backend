package com.ocupa.ocupa.service.impl;

import com.ocupa.ocupa.model.Portfolio;
import com.ocupa.ocupa.repository.PortfolioRepository;
import com.ocupa.ocupa.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {
    private final PortfolioRepository repository;

    @Override
    public List<Portfolio> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Portfolio> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Portfolio save(Portfolio portfolio) {
        return repository.save(portfolio);
    }

    @Override
    public void delete(Portfolio portfolio) {
        repository.delete(portfolio);
    }

    @Override
    public Optional<Portfolio> findByArtistaId(Integer artistaId) {
        return repository.findByArtistaId(artistaId);
    }
}

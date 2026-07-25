package com.ocupa.ocupa.service;

import com.ocupa.ocupa.model.Portfolio;
import java.util.List;
import java.util.Optional;

public interface PortfolioService {
    List<Portfolio> findAll();
    Optional<Portfolio> findById(Integer id);
    Portfolio save(Portfolio portfolio);
    void delete(Portfolio portfolio);
    Optional<Portfolio> findByArtistaId(Integer artistaId);
}

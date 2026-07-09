package com.ocupa.ocupa.repository;

import com.ocupa.ocupa.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Integer> {
    Optional<Portfolio> findByArtistaId(Integer artistaId);
}

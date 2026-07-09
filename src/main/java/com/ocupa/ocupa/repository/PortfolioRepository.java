package com.ocupa.ocupa.repository;

import com.ocupa.ocupa.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Integer> {
}

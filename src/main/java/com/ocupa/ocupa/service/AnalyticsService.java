package com.ocupa.ocupa.service;

import com.ocupa.ocupa.model.Analytics;
import java.util.List;
import java.util.Optional;

public interface AnalyticsService {
    List<Analytics> findAll();
    Optional<Analytics> findById(Integer id);
    Analytics save(Analytics analytics);
    void delete(Analytics analytics);
}

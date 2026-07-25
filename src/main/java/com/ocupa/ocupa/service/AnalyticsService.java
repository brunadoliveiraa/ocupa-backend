package com.ocupa.ocupa.service;

import com.ocupa.ocupa.model.Analytics;
import com.ocupa.ocupa.repository.AnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final AnalyticsRepository repository;

    public List<Analytics> findAll() {
        return repository.findAll();
    }

    public Optional<Analytics> findById(Integer id) {
        return repository.findById(id);
    }

    public Analytics save(Analytics analytics) {
        return repository.save(analytics);
    }

    public void delete(Analytics analytics) {
        repository.delete(analytics);
    }
}

package com.ocupa.ocupa.service.impl;

import com.ocupa.ocupa.model.Analytics;
import com.ocupa.ocupa.repository.AnalyticsRepository;
import com.ocupa.ocupa.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {
    private final AnalyticsRepository repository;

    @Override
    public List<Analytics> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Analytics> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Analytics save(Analytics analytics) {
        return repository.save(analytics);
    }

    @Override
    public void delete(Analytics analytics) {
        repository.delete(analytics);
    }
}

package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.model.Analytics;
import com.ocupa.ocupa.repository.AnalyticsRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AnalyticsRepository repo;
    public AnalyticsController(AnalyticsRepository repo) { this.repo = repo; }

    @PostMapping
    public Analytics create(@RequestBody Analytics a){ return repo.save(a); }

    @GetMapping
    public List<Analytics> all(){ return repo.findAll(); }
}

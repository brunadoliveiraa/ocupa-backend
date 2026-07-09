package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.model.Portfolio;
import com.ocupa.ocupa.repository.PortfolioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {
    private final PortfolioRepository repo;
    public PortfolioController(PortfolioRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Portfolio> all(){ return repo.findAll(); }

    @PostMapping
    public Portfolio create(@RequestBody Portfolio p){ return repo.save(p); }

    @PutMapping("/{id}")
    public ResponseEntity<Portfolio> update(@PathVariable Integer id, @RequestBody Portfolio p){
        return repo.findById(id).map(existing -> { p.setId(existing.getId()); return ResponseEntity.ok(repo.save(p)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){ return repo.findById(id).map(existing -> { repo.delete(existing); return ResponseEntity.ok().<Void>build(); }).orElse(ResponseEntity.notFound().build()); }
}

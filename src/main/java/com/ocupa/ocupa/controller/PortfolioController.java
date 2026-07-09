package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.model.Portfolio;
import com.ocupa.ocupa.model.PortfolioMedia;
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

    @GetMapping("/artista/{artistaId}")
    public ResponseEntity<Portfolio> getByArtista(@PathVariable Integer artistaId) {
        return repo.findByArtistaId(artistaId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Portfolio create(@RequestBody Portfolio p){
        if (p.getMediaItems() != null) {
            for (PortfolioMedia media : p.getMediaItems()) {
                media.setPortfolio(p);
            }
        }
        return repo.save(p);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Portfolio> update(@PathVariable Integer id, @RequestBody Portfolio p){
        return repo.findById(id).map(existing -> {
            p.setId(existing.getId());
            if (p.getMediaItems() != null) {
                for (PortfolioMedia media : p.getMediaItems()) {
                    media.setPortfolio(p);
                }
            }
            return ResponseEntity.ok(repo.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){ return repo.findById(id).map(existing -> { repo.delete(existing); return ResponseEntity.ok().<Void>build(); }).orElse(ResponseEntity.notFound().build()); }
}

package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.model.Portfolio;
import com.ocupa.ocupa.model.PortfolioMedia;
import com.ocupa.ocupa.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
@RequiredArgsConstructor
public class PortfolioController {
    private final PortfolioService service;

    @GetMapping
    public List<Portfolio> all(){
        return service.findAll();
    }

    @GetMapping("/artista/{artistaId}")
    public ResponseEntity<Portfolio> getByArtista(@PathVariable Integer artistaId) {
        return service.findByArtistaId(artistaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Portfolio create(@RequestBody Portfolio p){
        if (p.getMediaItems() != null) {
            for (PortfolioMedia media : p.getMediaItems()) {
                media.setPortfolio(p);
            }
        }
        return service.save(p);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Portfolio> update(@PathVariable Integer id, @RequestBody Portfolio p){
        return service.findById(id).map(existing -> {
            p.setId(existing.getId());
            if (p.getMediaItems() != null) {
                for (PortfolioMedia media : p.getMediaItems()) {
                    media.setPortfolio(p);
                }
            }
            return ResponseEntity.ok(service.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        return service.findById(id).map(existing -> {
            service.delete(existing);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}

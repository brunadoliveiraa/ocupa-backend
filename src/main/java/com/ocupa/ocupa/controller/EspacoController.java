package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.model.Espaco;
import com.ocupa.ocupa.repository.EspacoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/espacos")
public class EspacoController {
    private final EspacoRepository repo;
    public EspacoController(EspacoRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Espaco> all() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Espaco> get(@PathVariable Integer id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Espaco create(@RequestBody Espaco e) {
        if (e.getMediaItems() != null) {
            for (com.ocupa.ocupa.model.EspacoMedia media : e.getMediaItems()) {
                media.setEspaco(e);
            }
        }
        return repo.save(e);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Espaco> update(@PathVariable Integer id, @RequestBody Espaco e) {
        return repo.findById(id).map(existing -> {
            e.setId(existing.getId());
            if (e.getMediaItems() != null) {
                for (com.ocupa.ocupa.model.EspacoMedia media : e.getMediaItems()) {
                    media.setEspaco(e);
                }
            }
            return ResponseEntity.ok(repo.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return repo.findById(id).map(existing -> { repo.delete(existing); return ResponseEntity.ok().<Void>build(); }).orElse(ResponseEntity.notFound().build());
    }
}

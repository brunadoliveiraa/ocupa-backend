package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.model.Artista;
import com.ocupa.ocupa.repository.ArtistaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artistas")
public class ArtistaController {
    private final ArtistaRepository repo;
    public ArtistaController(ArtistaRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Artista> all() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Artista> get(@PathVariable Integer id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Artista create(@RequestBody Artista a) { return repo.save(a); }

    @PutMapping("/{id}")
    public ResponseEntity<Artista> update(@PathVariable Integer id, @RequestBody Artista a) {
        return repo.findById(id).map(existing -> {
            a.setId(existing.getId());
            return ResponseEntity.ok(repo.save(a));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return repo.findById(id).map(existing -> { repo.delete(existing); return ResponseEntity.ok().<Void>build(); }).orElse(ResponseEntity.notFound().build());
    }
}

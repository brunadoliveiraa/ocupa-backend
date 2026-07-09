package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.model.Oportunidade;
import com.ocupa.ocupa.repository.OportunidadeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/oportunidades")
public class OportunidadeController {
    private final OportunidadeRepository repo;
    public OportunidadeController(OportunidadeRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Oportunidade> all() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Oportunidade> get(@PathVariable Integer id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Oportunidade create(@RequestBody Oportunidade o) { return repo.save(o); }

    @PutMapping("/{id}")
    public ResponseEntity<Oportunidade> update(@PathVariable Integer id, @RequestBody Oportunidade o) {
        return repo.findById(id).map(existing -> {
            o.setId(existing.getId());
            return ResponseEntity.ok(repo.save(o));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return repo.findById(id).map(existing -> { repo.delete(existing); return ResponseEntity.ok().<Void>build(); }).orElse(ResponseEntity.notFound().build());
    }
}

package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.model.Evento;
import com.ocupa.ocupa.repository.EventoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {
    private final EventoRepository repo;
    public EventoController(EventoRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Evento> all() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Evento> get(@PathVariable Integer id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Evento create(@RequestBody Evento e) { return repo.save(e); }

    @PutMapping("/{id}")
    public ResponseEntity<Evento> update(@PathVariable Integer id, @RequestBody Evento e) {
        return repo.findById(id).map(existing -> { e.setId(existing.getId()); return ResponseEntity.ok(repo.save(e)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return repo.findById(id).map(existing -> { repo.delete(existing); return ResponseEntity.ok().<Void>build(); }).orElse(ResponseEntity.notFound().build());
    }
}

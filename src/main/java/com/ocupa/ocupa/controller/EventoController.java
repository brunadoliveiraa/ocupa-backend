package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.model.Evento;
import com.ocupa.ocupa.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
public class EventoController {
    private final EventoService service;

    @GetMapping
    public List<Evento> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evento> get(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Evento create(@RequestBody Evento e) {
        return service.save(e);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evento> update(@PathVariable Integer id, @RequestBody Evento e) {
        return service.findById(id).map(existing -> {
            e.setId(existing.getId());
            return ResponseEntity.ok(service.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return service.findById(id).map(existing -> {
            service.delete(existing);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}

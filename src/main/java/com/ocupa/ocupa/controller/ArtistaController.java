package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.model.Artista;
import com.ocupa.ocupa.service.ArtistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/artistas")
@RequiredArgsConstructor
public class ArtistaController {
    private final ArtistaService service;

    @GetMapping
    public List<Artista> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artista> get(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Artista create(@RequestBody Artista a) {
        return service.save(a);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artista> update(@PathVariable Integer id, @RequestBody Artista a) {
        return service.findById(id).map(existing -> {
            a.setId(existing.getId());
            return ResponseEntity.ok(service.save(a));
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

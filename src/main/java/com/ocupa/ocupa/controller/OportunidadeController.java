package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.model.Oportunidade;
import com.ocupa.ocupa.service.OportunidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/oportunidades")
@RequiredArgsConstructor
public class OportunidadeController {
    private final OportunidadeService service;

    @GetMapping
    public List<Oportunidade> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Oportunidade> get(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Oportunidade create(@RequestBody Oportunidade o) {
        return service.save(o);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Oportunidade> update(@PathVariable Integer id, @RequestBody Oportunidade o) {
        return service.findById(id).map(existing -> {
            o.setId(existing.getId());
            return ResponseEntity.ok(service.save(o));
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

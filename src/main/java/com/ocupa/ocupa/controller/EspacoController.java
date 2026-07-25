package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.model.Espaco;
import com.ocupa.ocupa.model.EspacoMedia;
import com.ocupa.ocupa.service.EspacoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/espacos")
@RequiredArgsConstructor
public class EspacoController {
    private final EspacoService service;

    @GetMapping
    public List<Espaco> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Espaco> get(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Espaco create(@RequestBody Espaco e) {
        if (e.getMediaItems() != null) {
            for (EspacoMedia media : e.getMediaItems()) {
                media.setEspaco(e);
            }
        }
        return service.save(e);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Espaco> update(@PathVariable Integer id, @RequestBody Espaco e) {
        return service.findById(id).map(existing -> {
            e.setId(existing.getId());
            if (e.getMediaItems() != null) {
                for (EspacoMedia media : e.getMediaItems()) {
                    media.setEspaco(e);
                }
            }
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

package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.model.Artista;
import com.ocupa.ocupa.model.User;
import com.ocupa.ocupa.service.ArtistaService;
import com.ocupa.ocupa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/artistas")
@RequiredArgsConstructor
public class ArtistaController {
    private final ArtistaService service;
    private final UserRepository userRepo;

    @GetMapping
    public List<Artista> all() {
        return service.findByStatus("APROVADO");
    }

    @GetMapping("/pendentes")
    public List<Artista> pendentes() {
        return service.findByStatus("PENDENTE");
    }

    @GetMapping("/meus")
    public ResponseEntity<Artista> meus() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().getArtistaId() != null) {
            return service.findById(userOpt.get().getArtistaId())
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.notFound().build();
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
            a.setStatus(existing.getStatus());
            a.setMotivoRejeicao(existing.getMotivoRejeicao());
            return ResponseEntity.ok(service.save(a));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<Artista> aprovar(@PathVariable Integer id) {
        return service.findById(id).map(existing -> {
            existing.setStatus("APROVADO");
            existing.setMotivoRejeicao(null);
            return ResponseEntity.ok(service.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/rejeitar")
    public ResponseEntity<Artista> rejeitar(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        return service.findById(id).map(existing -> {
            existing.setStatus("REJEITADO");
            existing.setMotivoRejeicao(body.get("motivoRejeicao"));
            return ResponseEntity.ok(service.save(existing));
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

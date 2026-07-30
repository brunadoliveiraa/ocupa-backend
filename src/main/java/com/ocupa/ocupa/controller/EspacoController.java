package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.model.Espaco;
import com.ocupa.ocupa.model.EspacoMedia;
import com.ocupa.ocupa.service.EspacoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/espacos")
@RequiredArgsConstructor
public class EspacoController {
    private final EspacoService service;

    @GetMapping
    public List<Espaco> all() {
        return service.findByStatus("APROVADO");
    }

    @GetMapping("/pendentes")
    public List<Espaco> pendentes() {
        return service.findByStatus("PENDENTE");
    }

    @GetMapping("/meus")
    public List<Espaco> meus(@RequestParam(required = false) String email) {
        if (email == null) {
            email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return service.findByCriadoPorEmail(email);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Espaco> get(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Espaco e) {
        if (e.getNome() == null || e.getNome().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Nome do espaço é obrigatório"));
        }
        if (service.existsByNome(e.getNome().trim())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Espaço já foi cadastrado com este nome"));
        }
        if (e.getMediaItems() != null) {
            for (EspacoMedia media : e.getMediaItems()) {
                media.setEspaco(e);
            }
        }
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (isAdmin) {
            e.setStatus("APROVADO");
        } else {
            e.setStatus("PENDENTE");
        }

        if (auth != null && auth.getPrincipal() instanceof String) {
            e.setCriadoPorEmail((String) auth.getPrincipal());
        }

        return ResponseEntity.ok(service.save(e));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Espaco e) {
        return service.findById(id).map(existing -> {
            if (e.getNome() == null || e.getNome().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Nome do espaço é obrigatório"));
            }
            
            Optional<Espaco> spaceWithSameName = service.findByNome(e.getNome().trim());
            if (spaceWithSameName.isPresent() && !spaceWithSameName.get().getId().equals(existing.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Espaço já foi cadastrado com este nome"));
            }

            e.setId(existing.getId());
            e.setStatus(existing.getStatus());
            e.setMotivoRejeicao(existing.getMotivoRejeicao());
            e.setCriadoPorEmail(existing.getCriadoPorEmail());
            
            if (e.getMediaItems() != null) {
                for (EspacoMedia media : e.getMediaItems()) {
                    media.setEspaco(e);
                }
            }
            return ResponseEntity.ok(service.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<Espaco> aprovar(@PathVariable Integer id) {
        return service.findById(id).map(existing -> {
            existing.setStatus("APROVADO");
            existing.setMotivoRejeicao(null);
            return ResponseEntity.ok(service.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/rejeitar")
    public ResponseEntity<Espaco> rejeitar(@PathVariable Integer id, @RequestBody Map<String, String> body) {
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

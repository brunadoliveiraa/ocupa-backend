package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.model.Oportunidade;
import com.ocupa.ocupa.service.OportunidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/oportunidades")
@RequiredArgsConstructor
public class OportunidadeController {
    private final OportunidadeService service;

    @GetMapping
    public List<Oportunidade> all() {
        return service.findByStatus("APROVADO");
    }

    @GetMapping("/pendentes")
    public List<Oportunidade> pendentes() {
        return service.findByStatus("PENDENTE");
    }

    @GetMapping("/meus")
    public List<Oportunidade> meus(@RequestParam(required = false) String email) {
        if (email == null) {
            email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return service.findByCriadoPorEmail(email);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Oportunidade> get(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Oportunidade create(@RequestBody Oportunidade o) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (isAdmin) {
            o.setStatus("APROVADO");
        } else {
            o.setStatus("PENDENTE");
        }

        if (auth != null && auth.getPrincipal() instanceof String) {
            o.setCriadoPorEmail((String) auth.getPrincipal());
        }
        
        return service.save(o);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Oportunidade> update(@PathVariable Integer id, @RequestBody Oportunidade o) {
        return service.findById(id).map(existing -> {
            o.setId(existing.getId());
            o.setStatus(existing.getStatus());
            o.setMotivoRejeicao(existing.getMotivoRejeicao());
            o.setCriadoPorEmail(existing.getCriadoPorEmail());
            return ResponseEntity.ok(service.save(o));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/aprovar")
    public ResponseEntity<Oportunidade> aprovar(@PathVariable Integer id) {
        return service.findById(id).map(existing -> {
            existing.setStatus("APROVADO");
            existing.setMotivoRejeicao(null);
            return ResponseEntity.ok(service.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/rejeitar")
    public ResponseEntity<Oportunidade> rejeitar(@PathVariable Integer id, @RequestBody Map<String, String> body) {
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

package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.model.Request;
import com.ocupa.ocupa.repository.RequestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {
    private final RequestRepository repo;
    public RequestController(RequestRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Request> all(){ return repo.findAll(); }

    @PostMapping
    public Request create(@RequestBody Request r){ return repo.save(r); }

    @PutMapping("/{id}")
    public ResponseEntity<Request> update(@PathVariable Integer id, @RequestBody Request r){
        return repo.findById(id).map(existing -> { r.setId(existing.getId()); return ResponseEntity.ok(repo.save(r)); }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){ return repo.findById(id).map(existing -> { repo.delete(existing); return ResponseEntity.ok().<Void>build(); }).orElse(ResponseEntity.notFound().build()); }
}

package com.ocupa.ocupa.controller;

import com.ocupa.ocupa.model.Request;
import com.ocupa.ocupa.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService service;

    @GetMapping
    public List<Request> all(){
        return service.findAll();
    }

    @GetMapping("/provider/{artistaId}")
    public List<Request> getByProvider(@PathVariable Integer artistaId) {
        return service.findByProviderId(artistaId);
    }

    @GetMapping("/requester/{email}")
    public List<Request> getByRequester(@PathVariable String email) {
        return service.findByRequesterContato(email);
    }

    @PostMapping
    public Request create(@RequestBody Request r){
        return service.save(r);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Request> update(@PathVariable Integer id, @RequestBody Request r){
        return service.findById(id).map(existing -> {
            r.setId(existing.getId());
            return ResponseEntity.ok(service.save(r));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        return service.findById(id).map(existing -> {
            service.delete(existing);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}

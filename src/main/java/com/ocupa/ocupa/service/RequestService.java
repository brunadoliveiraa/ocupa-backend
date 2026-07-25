package com.ocupa.ocupa.service;

import com.ocupa.ocupa.model.Request;
import com.ocupa.ocupa.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final RequestRepository repository;

    public List<Request> findAll() {
        return repository.findAll();
    }

    public Optional<Request> findById(Integer id) {
        return repository.findById(id);
    }

    public Request save(Request request) {
        return repository.save(request);
    }

    public void delete(Request request) {
        repository.delete(request);
    }

    public List<Request> findByProviderId(Integer providerId) {
        return repository.findByProviderId(providerId);
    }

    public List<Request> findByRequesterContato(String requesterContato) {
        return repository.findByRequesterContato(requesterContato);
    }
}

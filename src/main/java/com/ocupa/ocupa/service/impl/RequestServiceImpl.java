package com.ocupa.ocupa.service.impl;

import com.ocupa.ocupa.model.Request;
import com.ocupa.ocupa.repository.RequestRepository;
import com.ocupa.ocupa.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;

    @Override
    public List<Request> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Request> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Request save(Request request) {
        return repository.save(request);
    }

    @Override
    public void delete(Request request) {
        repository.delete(request);
    }

    @Override
    public List<Request> findByProviderId(Integer providerId) {
        return repository.findByProviderId(providerId);
    }

    @Override
    public List<Request> findByRequesterContato(String requesterContato) {
        return repository.findByRequesterContato(requesterContato);
    }
}

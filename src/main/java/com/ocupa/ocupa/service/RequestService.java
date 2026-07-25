package com.ocupa.ocupa.service;

import com.ocupa.ocupa.model.Request;
import java.util.List;
import java.util.Optional;

public interface RequestService {
    List<Request> findAll();
    Optional<Request> findById(Integer id);
    Request save(Request request);
    void delete(Request request);
    List<Request> findByProviderId(Integer providerId);
    List<Request> findByRequesterContato(String requesterContato);
}

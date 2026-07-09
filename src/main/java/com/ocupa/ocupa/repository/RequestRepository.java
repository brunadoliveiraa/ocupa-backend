package com.ocupa.ocupa.repository;

import com.ocupa.ocupa.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findByProviderId(Integer providerId);
    List<Request> findByRequesterContato(String requesterContato);
}

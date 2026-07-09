package com.ocupa.ocupa.repository;

import com.ocupa.ocupa.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Integer> {
}

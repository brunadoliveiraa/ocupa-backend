package com.ocupa.ocupa.repository;

import com.ocupa.ocupa.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventoRepository extends JpaRepository<Evento, Integer> {
    Optional<Evento> findByNome(String nome);
    boolean existsByNome(String nome);
    List<Evento> findByStatus(String status);
    List<Evento> findByCriadoPorEmail(String email);
}

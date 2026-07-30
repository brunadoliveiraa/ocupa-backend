package com.ocupa.ocupa.service;

import com.ocupa.ocupa.model.Evento;
import java.util.List;
import java.util.Optional;

public interface EventoService {
    List<Evento> findAll();
    Optional<Evento> findById(Integer id);
    Evento save(Evento evento);
    void delete(Evento evento);
    Optional<Evento> findByNome(String nome);
    boolean existsByNome(String nome);
    List<Evento> findByStatus(String status);
    List<Evento> findByCriadoPorEmail(String email);
}

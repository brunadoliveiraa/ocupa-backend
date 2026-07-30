package com.ocupa.ocupa.service;

import com.ocupa.ocupa.model.Espaco;
import java.util.List;
import java.util.Optional;

public interface EspacoService {
    List<Espaco> findAll();
    Optional<Espaco> findById(Integer id);
    Espaco save(Espaco espaco);
    void delete(Espaco espaco);
    Optional<Espaco> findByNome(String nome);
    boolean existsByNome(String nome);
    List<Espaco> findByStatus(String status);
    List<Espaco> findByCriadoPorEmail(String email);
}
